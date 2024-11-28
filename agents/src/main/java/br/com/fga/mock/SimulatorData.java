package br.com.fga.mock;

import br.com.fga.models.Position;
import br.com.fga.models.Simulation;

import java.util.List;

public class SimulatorData {

    public static final List<Simulation> SIMULATION_LIST = List.of(
        new Simulation(new Position(0, 0), VehicleData.get(0)),
        new Simulation(new Position(10, 30), VehicleData.get(1)),
        new Simulation(new Position(56, 80), VehicleData.get(2))
    );

}
