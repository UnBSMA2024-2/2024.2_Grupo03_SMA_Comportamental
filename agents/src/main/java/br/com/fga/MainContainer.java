package br.com.fga;

import br.com.fga.agents.AntAgent;
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

            for (int i = 0; i < 20; i++) {
                AgentController antAgentController = agentService.createAgent("AntAgent" + i, AntAgent.class.getName(), null);
                antAgentController.start();
                Thread.sleep(5000);
            }
        } catch (StaleProxyException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}