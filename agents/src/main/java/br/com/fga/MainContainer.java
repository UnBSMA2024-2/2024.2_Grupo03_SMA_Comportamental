package br.com.fga;

import br.com.fga.agents.TruckAgent;
import br.com.fga.agents.HelloWorldAgent;
import br.com.fga.agents.TruckManagerAgent;
import br.com.fga.models.Position;
import br.com.fga.models.Truck;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import br.com.fga.utils.ThreadUtils;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

    public static void main(String[] args) {
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

}