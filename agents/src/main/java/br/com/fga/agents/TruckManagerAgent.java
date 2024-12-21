package br.com.fga.agents;

import br.com.fga.exceptions.AgentException;
import br.com.fga.http.HttpClient;
import br.com.fga.mock.SimulatorData;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import br.com.fga.utils.ThreadUtils;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.Serial;

public class TruckManagerAgent extends Agent {

    @Serial
    private static final long serialVersionUID = 7792591193470292413L;

    private final AgentService agentService = AgentServiceImpl.getInstance();

    @Override
    protected void setup() {

        addBehaviour(new TruckBirthBehaviour());
        addBehaviour(new UpdateSimulationDataBehaviour());
    }

    private class TruckBirthBehaviour extends OneShotBehaviour {

        @Serial
        private static final long serialVersionUID = 684719216378673630L;

        @Override
        public void action() {
            SimulatorData.getSimulationData().forEach(simulation -> {
                AgentController truckAgentController = agentService
                        .createAgent("TruckAgent" + simulation.getVehicle().getId(), TruckAgent.class.getName(), new Object[] {
                                simulation.getPosition(),
                                simulation.getVehicle(),
                        });

                try {
                    truckAgentController.start();
                } catch (StaleProxyException e) {
                    throw new AgentException(e.getMessage());
                }
            });
        }
    }

    private class UpdateSimulationDataBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = 5598642300837181790L;

        @Override
        public void action() {
            HttpClient.post("http://localhost:8080/simulation/update", SimulatorData.getSimulationData());
            ThreadUtils.sleep(2);
        }
    }

}
