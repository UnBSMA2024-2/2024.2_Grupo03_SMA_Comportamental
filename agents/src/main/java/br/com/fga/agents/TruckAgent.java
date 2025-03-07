package br.com.fga.agents;

import br.com.fga.aco.Ant;
import br.com.fga.aco.GraphNode;
import br.com.fga.exceptions.AgentArgsException;
import br.com.fga.http.HttpClient;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.io.Serial;
import java.util.Arrays;
import java.util.List;

public class TruckAgent extends Agent {

    @Serial
    private final AgentService agentService = AgentServiceImpl.getInstance();

    private Ant ant;

    @Override
    protected void setup() {
        Object[] args = getArguments();

        if (args == null || args.length != 4) {
            throw new AgentArgsException("Erro no número de parâmentros.");
        }

        ant = (Ant) args[2];

        DFAgentDescription dfd = getDfAgentDescription();

        agentService.register(this, dfd);

        addBehaviour(new NotifyBirthBehaviour());
        addBehaviour(new StartTripBehaviour());
    }

    private DFAgentDescription getDfAgentDescription() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sdExplore = new ServiceDescription();
        sdExplore.setType("explore-map-service");
        sdExplore.setName("Explore-Map-Service");

        ServiceDescription sdPheromone = new ServiceDescription();
        sdPheromone.setType("release-pheromone-service");
        sdPheromone.setName("Release-Pheromone-Service");

        dfd.addServices(sdExplore);
        dfd.addServices(sdPheromone);
        return dfd;
    }

    @Override
    protected void takeDown() {
        agentService.deregister(this);

        DFAgentDescription[] ants = agentService.search(this, "explore-map-service");

        if (ants.length > 0) {
            List<String> agentsName = Arrays.stream(ants)
                    .map(it -> it.getName().getLocalName())
                    .toList();

            HttpClient.post("http://localhost:8080/ants/agents", agentsName);
        }

    }

    private class NotifyBirthBehaviour extends Behaviour {

        @Serial
        private static final long serialVersionUID = -1995519278844268633L;

        private boolean isDone;

        @Override
        public void action() {
            DFAgentDescription[] ants = agentService.search(getAgent(), "explore-map-service");

            if (ants != null && ants.length > 0) {
                List<String> agentsName = Arrays.stream(ants)
                        .map(it -> it.getName().getLocalName())
                        .toList();

                int responseCode = HttpClient.post("http://localhost:8080/ants/agents", agentsName);

                isDone = (responseCode == 200);
            }
        }

        @Override
        public boolean done() {
            return isDone;
        }

    }

    private class StartTripBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
            MessageTemplate messageTemplatePerformative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
            MessageTemplate messageTemplateSender = MessageTemplate.MatchSender(getAID("TruckManagerController"));

            ACLMessage message = receive(MessageTemplate.and(
                    messageTemplatePerformative,
                    messageTemplateSender
            ));

            if (message != null) {
                try {
                    Object contentObject = message.getContentObject();

                    if (contentObject instanceof GraphNode graphNode) {
                        int step = 1;

                        while (step <= 3) {
                            step = switch (step) {
                                case 1 -> {
                                    ant.setStart(graphNode);
                                    yield 2;
                                }
                                case 2 -> {
                                    ant.startTrip();
                                    yield 3;
                                }
                                case 3 -> {
                                    ant.applyPheromone();

                                    ACLMessage okMessage = new ACLMessage(ACLMessage.CONFIRM);
                                    okMessage.setContentObject("OK");
                                    okMessage.addReceiver(getAID("TruckManagerController"));
                                    send(okMessage);

                                    yield 4;
                                }
                                default -> step;
                            };
                        }
                    }
                } catch (UnreadableException | IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                block();
            }
        }
    }

}
