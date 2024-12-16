package br.com.fga.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.fga.models.City;
import br.com.fga.models.Route;
import br.com.fga.models.Truck;
import br.com.fga.models.Vehicle;

public class MockRunner {
    public static void main(String[] args) {
        RouteData.main(args);
        // CityData.getAllRoutes();
        // System.out.println();
        for (Vehicle vehicle : VehicleData.VEHICLE_LIST) {
            if (vehicle instanceof Truck) {
                Truck truck = (Truck) vehicle;
                System.out.println("Inicializando a viagem com o caminhão " + truck.getColor());

                startTrip (truck);
                System.out.println();
            }
        }
    }

    private static void startTrip(Truck truck) {
        List<City> visitedCities = new ArrayList<>();
        visitedCities.add(truck.getCurrentCity());
        double totalDistance = 0.0;

        while (visitedCities.size() < CityData.CITY_LIST.size()) {
            System.out.println("Cidade atual: " + truck.getCurrentCity().getName());
            City nextCity = getNextCity(visitedCities, truck.getCurrentCity());
            if (nextCity == null) {
                break;
            }
            System.out.println("Viajando de " + truck.getCurrentCity().getName() + " para " + nextCity.getName() +
                " (" + truck.getCurrentCity().distanceTo(nextCity) + "km)");
            totalDistance += truck.getCurrentCity().distanceTo(nextCity);
            truck.setCurrentCity(nextCity);
            visitedCities.add(nextCity);
        }
        System.out.println("Distância total percorrida: " + totalDistance);
    }

    private static City getNextCity(List<City> visitedCities, City currentCity) {
        List<Route> possibleRoutes = currentCity.getRoutes().stream()
            .filter(route -> !visitedCities.contains(route.getCityB()))
            .toList();

        //List<Route> possibleRoutes = currentCity.getRoutes();
        System.out.println("Rotas possíveis: " + possibleRoutes);
        
        for (Route route : possibleRoutes) {
            return route.getCityB();
        }

        return null;
    }
}
