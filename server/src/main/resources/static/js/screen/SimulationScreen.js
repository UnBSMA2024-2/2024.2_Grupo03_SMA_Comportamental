import {_nodes, _edges} from '../data/index.js'

class SimulationScreen {

  constructor(mainContent) {
    let nodes = new vis.DataSet(_nodes);
		let edges = new vis.DataSet(_edges);

		const container = mainContent;
		let data = { nodes, edges };
		let options = {
			autoResize: true,
			width: '1200px',
			height: '600px',
			interaction: {
				dragNodes: false,
			},
      edges: {
        arrows: 'to',
        scaling:{
          min: 1,
          max: 100,
          customScalingFunction: (min, max, total, value) => value / total
        }
      }
		};
		this.network = new vis.Network(container, data, options);
    this.edges = edges;
  }

  renderScreen(simulationData) {
    simulationData.forEach(city => {
      city.adjs.forEach(adj => {
        try {
          const from = _nodes.find(node=>node.label == city.name).id;
          const to = _nodes.find(node=>node.label == adj.name).id;
          const edgeId = _edges.find(edge=>edge.from==from && edge.to==to).id;
          this.edges.update([{id:edgeId, value:adj.pheromone*100}]);
        }
        catch(err) {
          console.error(err);
        }
        // console.log(edgeId);
      });
    });
  }

}

export default SimulationScreen;