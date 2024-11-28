package br.com.fga.mock;

import br.com.fga.models.Truck;
import br.com.fga.models.Vehicle;

import java.util.List;

public class VehicleData {

    public static final List<Vehicle> VEHICLE_LIST = List.of(
        new Truck(40.0, "red", 3),
        new Truck(60.0, "green", 5),
        new Truck(78.3, "blue", 6)
    );

    public static Vehicle get(int index) {
        return VEHICLE_LIST.get(index);
    }

}
