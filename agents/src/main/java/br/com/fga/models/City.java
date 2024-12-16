package br.com.fga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.Serial;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class City implements Serializable {

    @Serial
    private static final long serialVersionUID = -3614224928523365746L;

    private String name;
    private Position position;
    private List<Route> routes;

    public City(String name, Position position) {
        this.name = name;
        this.position = position;
        this.routes = new ArrayList<>();
    }

    public void addRoute(Route route) {
        this.routes.add(route);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public double distanceTo(City city) {
        double x = Math.abs(this.position.getX() - city.getPosition().getX());
        double y = Math.abs(this.position.getY() - city.getPosition().getY());
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public String toString() {
        return name;
    }
}