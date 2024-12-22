/*
* @autor MicheleZito
* @link https://github.com/MicheleZito
* @version 1.0
* @adaptation: @DouglasMonteles
* */

package br.com.fga.aco;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class Adjacency implements Serializable {

	@Serial
	private static final long serialVersionUID = -3808629388189781695L;

	private GraphNode node;
	private int distance;
    private float pheromone;
	
	public Adjacency(GraphNode gr, int dist) {
		this.node = gr;
		this.distance = dist;
		this.setPheromone(ShortestACOConstants.THETA0);
	}
	
	
	public void addPheromone(float pheromone) {
        this.setPheromone(Math.min((this.pheromone + pheromone), ShortestACOConstants.THETA_MAX));
	}

	public void evaporate() {
        this.setPheromone(Math.max(this.pheromone * (1.0F - ShortestACOConstants.RHO), ShortestACOConstants.THETA_MIN));
	}

    public int getLength() {
		return this.distance;
	}

    private void setPheromone(float pheromone) {
		this.pheromone = pheromone;
	}

	@Override
	public String toString() {
		return "Adjacency{" +
				"node=" + node.getName() +
				", distance=" + distance +
				", pheromone=" + pheromone +
				'}';
	}
}
