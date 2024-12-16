package br.com.fga;

import br.com.fga.agents.TruckAgent;
import br.com.fga.agents.MasterAgent;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import br.com.fga.mock.RouteData;
import br.com.fga.mock.CityData;
import br.com.fga.mock.VehicleData;
import br.com.fga.models.City;

import java.util.List;
import java.util.ArrayList;

import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import br.com.fga.models.Vehicle;

// import jdk.internal.agent.Agent;

public class MainContainer {
    private static final int NUM_AGENTS = VehicleData.VEHICLE_LIST.size();
    private static final int MAX_CYCLES = 5;

    public static void main(String[] args) {
        RouteData.main(args);
        List<City> cities = CityData.CITY_LIST;

        try {
            PrintStream fileStream = new PrintStream(new FileOutputStream("log.txt", true));
            System.setOut(fileStream);
            System.setErr(fileStream);

            AgentService agentService = AgentServiceImpl.getInstance();
            // AgentController rma = agentService.createRmaAgent(null);
            AgentController truckAgentController = null;

            AgentController masterAgentController = agentService.createAgent(
                "MasterAgent", 
                MasterAgent.class.getName(), 
                new Object[]{VehicleData.VEHICLE_LIST.size(), MAX_CYCLES}
            );

            try {
                System.out.println("Agente mestre criado e iniciado.\n");
                masterAgentController.start(); 
            } catch (StaleProxyException e) {
                System.err.println("Erro ao iniciar o agente mestre: " + e.getMessage());
                throw new RuntimeException(e);
            }

            // try {
            //     rma.start(); 
            // } catch (Exception e) {
            //     System.err.println("Erro ao iniciar o agente RMA: " + e.getMessage());
            //     throw new RuntimeException(e);
            // }

            for (Vehicle vehicle : VehicleData.VEHICLE_LIST) {
                String agentName = "TruckAgent_" + vehicle.getName();
                Object[] agentArgs = new Object[]{vehicle, MAX_CYCLES};
                truckAgentController = agentService.createAgent(
                    agentName, 
                    TruckAgent.class.getName(), 
                    agentArgs
                );
                try {
                    System.out.println("Agente " + agentName + " criado e iniciado.\n");
                    truckAgentController.start(); 
                } catch (StaleProxyException e) {
                    System.err.println("Erro ao iniciar o agente " + agentName + ": " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao configurar o log: " + e.getMessage());
            e.printStackTrace();
        }

        
    }
}