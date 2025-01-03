package br.com.fga.mock;

import br.com.fga.models.Position;
import br.com.fga.models.Simulation;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class SimulatorData implements Serializable {

    @Serial
    private static final long serialVersionUID = 5437469129638472440L;

    private static final List<Simulation> SIMULATION_LIST = List.of(
        new Simulation(new Position(0, 0), VehicleData.get(0))
//        new Simulation(new Position(0, 0), VehicleData.get(1)),
//        new Simulation(new Position(0, 0), VehicleData.get(2))
    );

    public static List<Simulation> getSimulationData() {
        return SIMULATION_LIST;
    }

}
