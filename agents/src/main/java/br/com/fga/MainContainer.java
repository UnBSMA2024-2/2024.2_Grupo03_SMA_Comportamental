package br.com.fga;

import br.com.fga.agents.TruckManagerAgent;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

    public static void bootstrap() {
        AgentService agentService = AgentServiceImpl.getInstance();

        AgentController truckManagerController = agentService.createAgent("TruckManagerController", TruckManagerAgent.class.getName(), null);

        AgentController rma = agentService.createRmaAgent(null);

        try {
            rma.start();
            truckManagerController.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        bootstrap();
    }

}