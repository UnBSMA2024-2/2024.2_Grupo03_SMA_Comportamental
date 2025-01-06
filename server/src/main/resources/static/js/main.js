import SockJsService from './services/SockJsService.js';

import SimulationScreen from './screen/SimulationScreen.js';

// Global variables
const mainContent = document.getElementById('app');
// End global variables

// Global services
const sockJsService = new SockJsService('/truck-updates');
sockJsService.connect();

let simulationScreen;
window.onload = function() {
  simulationScreen = new SimulationScreen(mainContent);
}
// End global services

// Topics to subscribe to
sockJsService.subscribeTopic('/topic/graph/updateNodes', (message) => {
  const simulationBody = JSON.parse(message.body);
  simulationScreen.renderScreen(simulationBody);

  let sentinel = 0;
  while(sentinel++ < 10) {
  console.log(simulationBody);
  }
});
// End topics to subscribe


// window.publishMessage = publishMessage;