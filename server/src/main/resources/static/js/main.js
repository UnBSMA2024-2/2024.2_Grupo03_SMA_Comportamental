import SockJsService from './services/SockJsService.js';

import SimulationScreen from './screen/SimulationScreen.js';

// Global variables
// const mainContent = document.getElementById('app');
// End global variables

// Global services
const sockJsService = new SockJsService('/truck-updates');
sockJsService.connect();

// const simulationScreen = new SimulationScreen(mainContent);
// End global services

function getRandomHexColor() {
  const randomHex = Math.floor(Math.random() * 16777215).toString(16);
  return `#${randomHex.padStart(6, '0')}`;
}

document.addEventListener('DOMContentLoaded', function () {
  // Gera o grafo
  var cy = window.cy = cytoscape({
    container: document.getElementById('cy'),

    // demo your layout
    layout: {
      name: 'euler',
      randomize: false,
      animate: false,
      // font: <https://stackoverflow.com/questions/54015729/cytoscape-js-spacing-between-nodes>
      spacingFactor: 4,

      // some more options here...
    },

    style: [
      {
        selector: 'node',
        style: {
          'content': 'data(name)',
          'background-color': '#126814',
          'text-valign': 'center',
          'text-halign': 'center',
          'color': '#fff',
          'padding': 20,
          'text-wrap': 'wrap',
        }
      },

      {
        selector: 'edge',
        style: {
          'width': 10,
          'line-color': '#000',
          'opacity': 0.3,
        }
      },

      {
        selector: ':selected',
        style: {

        }
      },

      {
        selector: '.highlighted',
        style: {
          'background-color': '#61bffc',
          'line-color': '#61bffc',
          'target-arrow-color': '#61bffc',
          'transition-property': 'background-color, line-color, target-arrow-color',
          'transition-duration': '0.5s'
        }
      },

      {
        selector: '.red',
        style: {
          'background-color': 'red',
          'line-color': 'red',
          'target-arrow-color': 'red',
          'transition-property': 'background-color, line-color, target-arrow-color',
          'transition-duration': '0.5s'
        }
      },

      {
        selector: '.truck',
        style: {
          'background-color': 'red',
          'line-color': 'red',
          'target-arrow-color': 'red',
          'transition-property': 'background-color, line-color, target-arrow-color',
          'transition-duration': '0.5s',
          'width': '3px',
          'height': '3px',
          'font-size': '12px',
        }
      },
    ],

    elements: fetch('http://localhost:8080/graph').then(async function (res) {
      const response = await res.json();
      console.log(response);

      const edges = [];

      const nodes = response.nodes.map((node, index) => {

        const nodeFormated = {
          "data": {
            name: node.node,
            "id": `${node.node}`,
            "sbgnbbox": {
              "x": 10,
              "y": 20,
              "w": "120.0",
              "h": "60.0"
            },
            "sbgnclass": "macromolecule",
            "sbgnlabel": "hexokinase",
            "sbgnstatesandinfos": [],
            "ports": [],
          },
          "position": {
            "x": 10,
            "y": 20,
          },
          "group": "nodes",
          "removed": false,
          "selected": false,
          "selectable": true,
          "locked": false,
          "grabbable": true,
          "classes": ""
        };

        return nodeFormated;
      });

      response.nodes.forEach((node, index) => {
        const nodeName = node.node;

        node.edges.forEach(edge => {
          const formattedEdge = {
            "data": {
              "id": `edge${nodeName}-${edge.node}`,
              "sbgnclass": "production",
              "sbgncardinality": 0,
              "source": nodeName,
              "target": edge.node,
              "portsource": nodeName,
              "porttarget": edge.node,
            },
            "position": {},
            "group": "edges",
            "removed": false,
            "selected": false,
            "selectable": true,
            "locked": false,
            "grabbable": true,
            "classes": [],
          };

          edges.push(formattedEdge);
        });
      });

      return [
        ...nodes,
        ...edges,
      ];
    })
  });
  // --

  let bestPath = {
    nodes: [],
    pheromones: [],
  };

  let lenBestPath = 0;

  // Topics to subscribe to
  sockJsService.subscribeTopic('/topic/graph/updateNodes', (message) => {
    const simulationBody = JSON.parse(message.body);
    // simulationScreen.renderScreen(simulationBody);
    const path = [];
    const pheromones = [];

    console.log(simulationBody);
    simulationBody.forEach(node => {
      //console.log("No inicial: " + node.name);
      path.push(node.name);
      pheromones.push(node.adjs.filter(edge => edge.name != node.name && edge.pheromone > 0.33)[0].pheromone);
      // console.log("caminho percorrido:");
      // console.log(node.adjs.map(edge => edge.name));
    });

    if (bestPath.pheromones.length == 0 || bestPath.pheromones.length > pheromones.length) {
      bestPath = {
        nodes: path,
        pheromones: pheromones,
      };
    }

    if (lenBestPath < path.length) {
      lenBestPath = path.length;
    }

    console.log(path);
    console.log(pheromones);
    console.log(bestPath);

    //setTimeout(() => {
    const truckId = new Date().getTime().toString();

    const nodeName1 = path.shift();
    const nodeName2 = path.shift();

    let nodeA = cy.getElementById(nodeName1);
    let nodeB = cy.getElementById(nodeName2);

    let posA = nodeA.position();
    let posB = nodeB.position();

    cy.add({
      group: 'nodes',
      data: { id: truckId, name: 'truck' },
      style: {
        "background-color": getRandomHexColor()
      },
      position: {
        x: posA.x,
        y: posA.y,
      },
      classes: ['truck']
    });

    // Defina a duração da animação e o número de passos
    let duration = 5000;
    let steps = 100;
    let interval = duration / steps;
    let currentStep = 0;

    const animation = setInterval(() => {
      const truckNode = cy.getElementById(truckId);

      currentStep++;

      const t = currentStep / steps;
      const x = posA.x + t * (posB.x - posA.x);
      const y = posA.y + t * (posB.y - posA.y);

      truckNode.position({
        x: x,
        y: y,
      });

      // Quando chegar no nó alvo
      if (currentStep >= steps) {
        // Adiciona feromônio no caminho
        const n1 = nodeA.data().name;
        const n2 = nodeB.data().name;

        let edgeName = `edge${n1}-${n2}`;

        if (cy.getElementById(edgeName).classes().length <= 0) {
          if (pheromones[0] >= 1.3) {
            cy.getElementById(edgeName).classes("red");
          } else {
            cy.getElementById(edgeName).classes("highlighted");
          }
        } else {
          if (pheromones[0] >= 1.3) {
            cy.getElementById(edgeName).classes("red");
          }
        }

        if (path.length > lenBestPath) {
          cy.getElementById(edgeName).classes("highlighted");
        }

        if (path.length > 0) {
          duration = 5000;
          steps = 100;
          interval = duration / steps;
          currentStep = 0;

          nodeA = nodeB;
          posA = posB;

          nodeB = cy.getElementById(path.shift());
          posB = nodeB.position();
        } else {
          clearInterval(animation);
        }
      }
    }, (Math.random() * (150 - 50) + 50));
    //}, 2000);

  });
  // End topics to subscribe

  // const path = ["Como", "Monza", "Lecco", "Bergamo", "Brescia"];


});
