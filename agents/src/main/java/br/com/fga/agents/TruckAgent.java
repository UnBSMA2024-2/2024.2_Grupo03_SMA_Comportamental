package br.com.fga.agents;

import br.com.fga.models.City;
import br.com.fga.models.Route;
import br.com.fga.models.Truck;
import br.com.fga.mock.CityData;
import br.com.fga.mock.RouteData;

import jade.core.*;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import java.util.List;
import java.util.ArrayList;

import br.com.fga.models.Position;

public class TruckAgent extends Agent {
    
    private Truck truck;
    private int MAX_CYCLES = 0;

    private int iterationCount = 0;
    private static final double alpha = 1.0; // Importância do feromônio
    private static final double beta = 5.0; // Importância da visibilidade
    private double getDistanceTraveled = 0;
    private boolean finishedCycle = false;
    private boolean bestSolution = false;
    private boolean canDoNextInteration = false;
    private boolean updatedPheromone = false;
    private boolean waitUpdatePheromone = false;
    private List<Route> routesToUpdate = new ArrayList<>();

    @Override
    protected void setup() {
        Object[] args = getArguments(); 
        
        if (args != null && args.length > 1) {
            truck = (Truck) args[0];
            MAX_CYCLES = (int) args[1];
        }
        System.out.println("Caminhão inicializado: " + truck.getName());

        addBehaviour (new ExploreRouteBehaviour());
        addBehaviour (new UpdatePheromoneBehaviour());
    }

    @Override
    protected void takeDown() {
        System.out.println("Agente " + truck.getName() + " finalizado.");
    }

    private class ExploreRouteBehaviour extends Behaviour {
        private boolean isFinished = false;
        private List<City> cities = new ArrayList<>(CityData.CITY_LIST); 

        @Override
        public void action() {
            System.out.println("Iteração " + (iterationCount+1) + "/" + MAX_CYCLES + " para " + truck.getName());
            cities.remove(truck.getCurrentCity()); 
            System.out.println(truck.getName() + " está explorando as rotas da cidade " + truck.getCurrentCity().getName());

            List<City> possibCities = truck.getCurrentCity().getRoutes().stream()
                    .map(Route::getCityB)
                    .filter(cities::contains)
                    .toList();
            System.out.println("Rotas disponíveis para " + truck.getName() + ": " + possibCities);

            if (possibCities.isEmpty()) {
                System.out.println("Não há mais cidades para visitar.");
                finishedCycle = true;
                isFinished = true;
                return;
            }

            List<Route> routes = truck.getCurrentCity().getRoutes().stream()
                    .filter(route -> possibCities.contains(route.getCityB()))
                    .toList();

            City nextCity = getNextCity(routes);
            routesToUpdate.add(truck.getCurrentCity().getRoutes().stream()
                    .filter(route -> route.getCityB().equals(nextCity))
                    .findFirst()
                    .orElse(null));

            getDistanceTraveled += truck.getCurrentCity().distanceTo(nextCity);

            truck.setCurrentCity(nextCity);
            System.out.println(truck.getName() + " está indo para a cidade " + truck.getCurrentCity().getName());
        }

        @Override
        public boolean done() {
            return isFinished;
        }
    }

    private class UpdatePheromoneBehaviour extends CyclicBehaviour {        
        @Override
        public void action() {
            if (finishedCycle) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("MasterAgent", AID.ISLOCALNAME));
                msg.setContent(getLocalName() + ";" + getDistanceTraveled);
                finishedCycle = false;
                bestSolution = true;
                send(msg);
            }

            if (bestSolution) {
                System.out.println(truck.getName() + " está esperando para atualizar (ou não) o feromônio.");

                ACLMessage response = receive();

                while (response == null) {
                    response = receive();
                }

                if ("UPDATE_PHEROMONE".equals(response.getContent())) {
                    System.out.println(truck.getName() + " está atualizando o feromônio.");
                    for (Route route : RouteData.ROUTE_LIST) {
                        if (routesToUpdate.contains(route)) {
                            route.updatePheromone(1.0);
                        }
                        else {
                            route.updatePheromone(0.0);
                        }
                        System.out.println(truck.getName() + " atualizou o feromônio da rota " + route + " para " + route.getPheromone());
                    }

                    bestSolution = false;
                    updatedPheromone = true;
                }
                else if ("DO_NOTHING".equals(response.getContent())) {
                    System.out.println(truck.getName() + " não precisa atualizar o feromônio. Mas está esperando os feromônios serem atualizados.");
                    bestSolution = false;
                    waitUpdatePheromone = true;
                }
            }

            if (updatedPheromone) {
                ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
                msg.addReceiver(new AID("MasterAgent", AID.ISLOCALNAME));
                msg.setContent(getLocalName() + ";" + "PHEROMONE_UPDATED");
                updatedPheromone = false;
                send(msg);
                canDoNextInteration = true;
            }

            if (waitUpdatePheromone) {
                ACLMessage response = receive();
                while (response == null) {
                    response = receive();
                }

                if ("DO_NEXT_INTERATION".equals(response.getContent())) {
                    waitUpdatePheromone = false;
                    canDoNextInteration = true;
                }
            }

            if (canDoNextInteration) {
                iterationCount++;
                if (iterationCount < MAX_CYCLES) {
                    System.out.println(truck.getName() + " está pronto para a próxima iteração.");
                    canDoNextInteration = false;
                    routesToUpdate.clear();
                    getDistanceTraveled = 0.0;
                    addBehaviour(new ExploreRouteBehaviour());
                } else {
                    System.out.println(truck.getName() + " completou todas as iterações.");
                    doDelete();
                }
            }
        }
    }

    private City getNextCity(List<Route> routes) {
        double[] probabilities = new double[routes.size()];
        double sum = 0;
        double bestProbability = 0;
        int bestRoute = 0;

        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            double pheromone = route.getPheromone();
            double visibility = 1 / route.getDistance();
            sum += Math.pow(pheromone, alpha) * Math.pow(visibility, beta);
        }
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            double pheromone = route.getPheromone();
            double visibility = 1 / route.getDistance();
            probabilities[i] = (Math.pow(pheromone, alpha) * Math.pow(visibility, beta)) / sum;
            System.out.println(truck.getName() + " irá para " + route.getCityB().getName() + "?\nDistância: " + route.getDistance() + "\nFeromônio: " + pheromone + "\nProbabilidade: " + probabilities[i]);
        }

        for (int i = 0; i < probabilities.length; i++) {
            if (probabilities[i] > bestProbability) {
                bestProbability = probabilities[i];
                bestRoute = i;
            }
        }
        return routes.get(bestRoute).getCityB();
    }
}