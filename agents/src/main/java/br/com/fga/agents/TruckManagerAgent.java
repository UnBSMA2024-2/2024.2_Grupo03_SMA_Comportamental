package br.com.fga.agents;

import br.com.fga.aco.*;
import br.com.fga.exceptions.AgentException;
import br.com.fga.http.HttpClient;
import br.com.fga.mock.SimulatorData;
import br.com.fga.observables.Observer;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.io.Serial;
import java.util.Vector;

public class TruckManagerAgent extends Agent implements Observer {

    @Serial
    private static final long serialVersionUID = 7792591193470292413L;

    private final AgentService agentService = AgentServiceImpl.getInstance();

    private final Graph graph = new Graph();
    private ShortestACO aco;

    private int interactions = 0;
    /*
     * Controla o número de vezes que o nó inicial é enviado, evitando que um novo nó
     * seja enviado antes de receber a confirmação do envio anterior. Basicamente esperamos
     * todas as formigas terminarem o trajeto antes de iniciar o próximo
     * */
    private boolean isWaitingForAnswer = false;

    @Override
    protected void setup() {
        graph.buildGraph();

        graph.defineStart("Como");
        graph.defineEnd("Pavia");

        aco = new ShortestACO(graph);

        SequentialBehaviour acoSequentialBehavior = new SequentialBehaviour();
        acoSequentialBehavior.addSubBehaviour(new TruckBirthBehaviour());
        acoSequentialBehavior.addSubBehaviour(new ACOBehaviour());

        addBehaviour(acoSequentialBehavior);
        addBehaviour(new SendGraphDataBehaviour());
        addBehaviour(new ACOWaitForResponseBehaviour());

        graph.addObserver(this);
    }

    private class TruckBirthBehaviour extends OneShotBehaviour {

        @Serial
        private static final long serialVersionUID = 684719216378673630L;

        @Override
        public void action() {
            SimulatorData.getSimulationData().forEach(simulation -> {
                AgentController truckAgentController = agentService
                        .createAgent("TruckAgent" + simulation.getVehicle().getId(), TruckAgent.class.getName(), new Object[]{
                                simulation.getPosition(),
                                simulation.getVehicle(),
                                new Ant(aco),
                                graph,
                        });

                try {
                    truckAgentController.start();
                } catch (StaleProxyException e) {
                    throw new AgentException(e.getMessage());
                }
            });
        }
    }

    private class ACOBehaviour extends Behaviour {

        @Serial
        private static final long serialVersionUID = -98891813951915183L;

        private final long startTime;

        public ACOBehaviour() {
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public void action() {
            // Inicia a exploração das formigas
            try {
                if (!isWaitingForAnswer) {
                    DFAgentDescription[] result = agentService.search(getAgent(), "explore-map-service");

                    if (result.length > 0) {
                        // TODO: tratar caso do agente Truck recusar a requisição
                        ACLMessage startTripMessage = new ACLMessage(ACLMessage.REQUEST);

                        for (DFAgentDescription agentDescription : result) {
                            AID serviceProvider = agentDescription.getName();
                            startTripMessage.addReceiver(serviceProvider);
                        }

                        startTripMessage.setContentObject(graph.getStart());
                        send(startTripMessage);
                        isWaitingForAnswer = true;
                    } else {
                        System.out.println("Nenhum agente encontrado, mas não fique triste, nem tudo são flores.");
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        @Override
        public boolean done() {
            boolean isDone = (interactions >= ShortestACOConstants.MAX_ITERATIONS) && (System.currentTimeMillis() - startTime <= ShortestACOConstants.MAX_TIME_TO_WAIT);

            if (isDone) {
                if (aco.getBestPathLength() != 0) {
                    System.out.println("\n" + "Shortest Path: ");
                    Vector<GraphNode> nodesOnPath = aco.getBestPath().getPathNodes();

                    for (GraphNode t : nodesOnPath) {
                        System.out.print(" -> " + t.getName());
                    }

                    System.out.println("\n\n" + "Total Distance: " + aco.getBestPath().getTotaLength());
                } else {
                    System.out.println("\n\n" + "No path found. Maybe The node is not connected");
                }

                return true;
            }

            graph.updateEdges();

            return false;
        }
    }

    private class ACOWaitForResponseBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = 4370136558332471150L;

        private int contResponses = 0;

        @Override
        public void action() {
            DFAgentDescription[] result = agentService.search(getAgent(), "explore-map-service");

            MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
            ACLMessage message = receive(messageTemplate);

            if (message == null) {
                block();
            } else {
                String content = null;
                try {
                    content = (String) message.getContentObject();

                    if (content.equals("OK")) {
                        interactions++;
                        contResponses++;

                        if (contResponses == result.length) {
                            isWaitingForAnswer = false;
                            contResponses = 0;
                        }
                    }
                } catch (UnreadableException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private class SendGraphDataBehaviour extends Behaviour {

        @Serial
        private static final long serialVersionUID = 5598642300837181790L;

        private int status = -1;

        @Override
        public void action() {
            status = HttpClient.post("http://localhost:8080/graph/update", graph);

            if (status != 200) {
                block(PeriodBehaviour.ONE_SECOND.value());
            }
        }

        @Override
        public boolean done() {
            return status == 200;
        }
    }

    @Override
    public void update() {
//        int status;
//
//        do {
//            status = HttpClient.post("http://localhost:8080/graph/update", graph);
//        } while (status != 200);
    }
}
