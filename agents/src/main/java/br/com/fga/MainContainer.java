package br.com.fga;

import br.com.fga.agents.HelloWorldAgent;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

public class MainContainer {

    public static void main(String[] args) {
        AgentService agentService = AgentServiceImpl.getInstance();

        AgentController helloWorldAgentController = agentService.createAgent("HelloWorldAgent", HelloWorldAgent.class.getName(), null);

        AgentController rma = agentService.createRmaAgent(null);

        try {
            rma.start();
            helloWorldAgentController.start();
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        }
    }

}