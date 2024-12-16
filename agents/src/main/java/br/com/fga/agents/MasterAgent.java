package br.com.fga.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class MasterAgent extends Agent {
    private static final long serialVersionUID = 1L;
    private Map<String, Double> truckDistances = new HashMap<>();
    private int completedAgents = 0;
    private int TOTAL_AGENTS = 0;
    private int MAX_CYCLES = 0;
    private int step = 0;

    @Override
    protected void setup() {
        System.out.println("Agente mestre iniciado.");

        Object[] args = getArguments();
        if (args != null && args.length > 0) {
            TOTAL_AGENTS = (int) args[0];
            MAX_CYCLES = (int) args[1];
        }

        System.out.println("Total de agentes: " + TOTAL_AGENTS);

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                if (MAX_CYCLES == 0) {
                    System.out.println("Ciclos de execução finalizados.");
                    doDelete();
                    return;
                }
            
                if (step == 0) {
                    findBestSolution();
                } else if (step == 1) {
                    updatePheromone();
                }
            }
        });
    }

    private void findBestSolution() {
        ACLMessage msg = receive();

        if (msg == null) {
            return;
        }
        String[] content = msg.getContent().split(";");
        if (content.length != 2) {
            System.out.println("Mensagem inválida.");
            return;
        }
        String truckName = content[0];
        Double distance = Double.parseDouble(content[1]);
        System.out.println("Mensagem recebida:\nO caminhão " + truckName + " percorreu " + distance + " km no total.");

        truckDistances.put(truckName, distance);
        completedAgents++;

        if (completedAgents == TOTAL_AGENTS) {
            System.out.println("Todos os agentes completaram a rota.");
            String bestTruck = truckDistances.entrySet().stream()
                .min((entry1, entry2) -> entry1.getValue().compareTo(entry2.getValue()))
                .get()
                .getKey();

            System.out.println("Melhor caminhão: " + bestTruck);

            for (String truck : truckDistances.keySet()) {
                ACLMessage updateMsg = new ACLMessage(ACLMessage.INFORM);
                updateMsg.addReceiver(new AID(truck, AID.ISLOCALNAME));
                
                if (truck.equals(bestTruck)) {
                    updateMsg.setContent("UPDATE_PHEROMONE"); 
                    System.out.println("Enviando comando para " + truck + " atualizar os feromônios.");
                } else {
                    updateMsg.setContent("DO_NOTHING"); 
                    System.out.println("Enviando comando para " + truck + " não fazer nada.");
                }
                
                send(updateMsg);
            }
            completedAgents = 0;
            step = 1;
        }
    }           

    private void updatePheromone() {
        ACLMessage msg = receive();

        if (msg == null) {
            return;
        }

        if ("PHEROMONE_UPDATED".equals(msg.getContent())) {
            System.out.println("Feromônios atualizados.");
        }

        for (String truck : truckDistances.keySet()) {
            ACLMessage updateMsg = new ACLMessage(ACLMessage.INFORM);
            updateMsg.addReceiver(new AID(truck, AID.ISLOCALNAME));
            updateMsg.setContent("DO_NEXT_INTERATION");
            send(updateMsg);
        }

        truckDistances.clear();
        step = 0;
        MAX_CYCLES--;
    }
    @Override
    protected void takeDown() {
        System.out.println("Agente " + getLocalName() + " finalizado.");
    }
}