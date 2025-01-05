package br.com.fga.mock;

import br.com.fga.models.Truck;
import br.com.fga.models.Vehicle;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class VehicleData implements Serializable {

    @Serial
    private static final long serialVersionUID = 2197516400193141803L;

    public static final List<Vehicle> VEHICLE_LIST = List.of(
            new Truck(40.0, "red", 3),
            new Truck(60.0, "green", 5),
            new Truck(78.3, "blue", 6),
            new Truck(40.0, "red", 3),
            new Truck(60.0, "green", 5),
            new Truck(78.3, "blue", 6),
            new Truck(40.0, "red", 3),
            new Truck(60.0, "green", 5),
            new Truck(78.3, "blue", 6),
            new Truck(40.0, "red", 3),
            new Truck(60.0, "green", 5),
            new Truck(78.3, "blue", 6)
    );

    public static Vehicle get(int index) {
        return VEHICLE_LIST.get(index);
    }

}
