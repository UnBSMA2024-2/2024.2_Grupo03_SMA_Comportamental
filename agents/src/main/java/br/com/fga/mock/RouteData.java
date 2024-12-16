package br.com.fga.mock;

import java.util.ArrayList;
import java.util.List;

import br.com.fga.models.Route;

public class RouteData {
    public static List<Route> ROUTE_LIST = new ArrayList<>();
    public static void main(String[] args) {
        for (int i = 0; i < CityData.CITY_LIST.size(); i++) {
            for (int j = 0; j < CityData.CITY_LIST.size(); j++) {
                if (i != j) {
                    // System.out.println("Rota de " + CityData.get(i) + " para " + CityData.get(j));
                    CityData.get(i).addRoute(new Route(CityData.get(i), CityData.get(j)));
                }
            }
        }
        updateRouteList();
    }

    public static Route get(int i) {
        return ROUTE_LIST.get(i);
    }

    private static void updateRouteList() {
        for (int i = 0; i < CityData.CITY_LIST.size(); i++) {
            for (Route route : CityData.get(i).getRoutes()) {
                ROUTE_LIST.add(route);
            }
        }
    }

    
}
