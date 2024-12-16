package br.com.fga.mock;

import java.util.List;

import br.com.fga.models.Truck;
import br.com.fga.models.Vehicle;

public class VehicleData {

    public static final List<Vehicle> VEHICLE_LIST = List.of(
        new Truck("Alvin", 40.0, "red", 3, CityData.get(0)),
        new Truck("Theodore", 60.0, "green", 5, CityData.get(1)),
        new Truck("Simon", 78.3, "blue", 6, CityData.get(2))
    );

    public static Vehicle get(int index) {
        return VEHICLE_LIST.get(index);
    }

}
