package br.com.fga.agents;

import br.com.fga.aco.*;
import br.com.fga.exceptions.AgentException;
import br.com.fga.http.HttpClient;
import br.com.fga.mock.SimulatorData;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.IOException;
import java.io.Serial;
import java.util.Vector;

public class TruckManagerAgent extends Agent {

    @Serial
    private static final long serialVersionUID = 7792591193470292413L;

    private final AgentService agentService = AgentServiceImpl.getInstance();

    private final Graph graph = new Graph();
    private ShortestACO aco;

    @Override
    protected void setup() {
        graph.buildGraph();

        graph.setStart("Como");
        graph.setEnd("Milano");

        aco = new ShortestACO(graph);

        SequentialBehaviour acoSequentialBehavior = new SequentialBehaviour();
        acoSequentialBehavior.addSubBehaviour(new TruckBirthBehaviour());
        acoSequentialBehavior.addSubBehaviour(new ACOBehaviour());

        addBehaviour(acoSequentialBehavior);
        addBehaviour(new UpdateSimulationDataBehaviour(this, PeriodBehaviour.ONE_SECOND.value()));
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

        private int interactions = 0;

        private DFAgentDescription[] result = null;

        private final long startTime;

        public ACOBehaviour() {
            this.startTime = System.currentTimeMillis();
        }

        @Override
        public void action() {
            // Inicia a exploração das formigas
            try {
                result = agentService.search(getAgent(), "explore-map-service");

                if (result.length > 0) {
                    // TODO: tratar caso do agente Truck recusar a requisição
                    AID serviceProvider = result[0].getName();
                    ACLMessage startTripMessage = new ACLMessage(ACLMessage.REQUEST);

                    startTripMessage.addReceiver(serviceProvider);
                    startTripMessage.setContentObject(graph.getStart());

                    send(startTripMessage);
                } else {
                    System.out.println("Nenhum agente encontrado, mas não fique triste, nem tudo são flores.");
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
                    Vector<GraphNode> nodesOnPath = aco.getBestPath().getNodes();

                    for (GraphNode t : nodesOnPath) {
                        System.out.print(" -> " + t.getName());
                    }

                    System.out.println("\n\n" + "Total Distance: " + aco.getBestPath().getTotaLength());
                    return true;
                } else {
                    System.out.println("\n\n" + "No path found. Maybe The node is not connected");
                    return true;
                }
            }

            graph.updateEdges();
            interactions++;

            return false;
        }
    }

    private static class UpdateSimulationDataBehaviour extends TickerBehaviour {

        @Serial
        private static final long serialVersionUID = 5598642300837181790L;

        public UpdateSimulationDataBehaviour(Agent a, long period) {
            super(a, period);
        }

        @Override
        protected void onTick() {
            HttpClient.post("http://localhost:8080/simulation/update", SimulatorData.getSimulationData());
        }
    }

}
