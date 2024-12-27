import SockJsService from './services/SockJsService.js';

import SimulationScreen from './screen/SimulationScreen.js';

// Global variables
const mainContent = document.getElementById('app');
// End global variables

// Global services
const sockJsService = new SockJsService('/truck-updates');
sockJsService.connect();

const simulationScreen = new SimulationScreen(mainContent);
// End global services

// Topics to subscribe to
sockJsService.subscribeTopic('/topic/graph/updates', (message) => {
  const simulationBody = JSON.parse(message.body);
  simulationScreen.renderScreen(simulationBody);

  console.log(simulationBody);
});
// End topics to subscribe


// window.publishMessage = publishMessage;