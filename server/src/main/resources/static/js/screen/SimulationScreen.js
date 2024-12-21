class SimulationScreen {

  constructor(mainContent) {
    this._mainContent = mainContent;
  }
  renderScreen(simulationData) {
    this._mainContent.innerHTML += `<p>${JSON.stringify(simulationData)}</p>`;
  }
}

export default SimulationScreen;