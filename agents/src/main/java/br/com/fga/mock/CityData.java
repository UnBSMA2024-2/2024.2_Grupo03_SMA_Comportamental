package br.com.fga.mock;

import br.com.fga.models.City;
import br.com.fga.models.Route;
import br.com.fga.models.Position;

import java.util.List;

public class CityData {

    public static final List<City> CITY_LIST = List.of(
        new City("Brasília", new Position(15.0, 20.0)),
        new City("Goiânia", new Position(20.0, 25.0)),
        new City("São Paulo", new Position(25.0, 30.0))
    );

    public static City get(int index) {
        return CITY_LIST.get(index);
    }

    public static void getAllRoutes() {
        for (int i = 0; i < CITY_LIST.size(); i++) {
            City city = CITY_LIST.get(i);
            System.out.println("Cidade " + i + ": " + city);
            List<Route> routes = city.getRoutes();
            System.out.println("Rotas: " + routes);
        }
    }
}
