package br.com.fga.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.io.Serial;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Route implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private City cityA;        
    private City cityB;        
    private double distance;   
    private double pheromone;
    private double evaporationRate = 0.5;  

    public Route(City cityA, City cityB) {
        this.cityA = cityA;
        this.cityB = cityB;
        this.distance = cityA.distanceTo(cityB);
        this.pheromone = 1.0;
    }

    public void updatePheromone(double delta) {
        this.pheromone = (1 - evaporationRate) * this.pheromone + delta;
    }

    @Override
    public String toString() {
        return cityA + " -> " + cityB;
    }
}
