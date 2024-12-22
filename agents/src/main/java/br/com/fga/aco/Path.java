/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import lombok.Data;
import lombok.Getter;

import java.util.Vector;

@Data
public class Path {
	@Getter
    private int totaLength;
	private final Vector<GraphNode> pathNodes;
	
	
	public Path() {
		this.pathNodes = new Vector<>();
		this.totaLength = 0;
	}
	
	public void releasePheromone(float bestLength) {
		float pheromoneToApply = (bestLength / this.totaLength);

		for (int i = 0; i < (pathNodes.size() - 1); i++) {
			this.pathNodes.elementAt(i).updatePheromoneOnEdge(this.pathNodes.elementAt(i+1), pheromoneToApply);
			this.pathNodes.elementAt(i+1).updatePheromoneOnEdge(this.pathNodes.elementAt(i), pheromoneToApply);
		}

		System.out.println("Feromonio aplicado: " + this.pathNodes);
		// TODO: Enviar feromonio atualizado para o frontend
	}
	
	public void add(GraphNode node) {
		this.pathNodes.add(node);
	}


    public void calculateTotaLength() {
		for (int i = 0; i < this.pathNodes.size() -1; i++) {
			this.setTotaLength(this.getTotaLength() + this.pathNodes.elementAt(i).getDistanceFromNode(this.pathNodes.elementAt(i+1)));
		}
	}

	private void setTotaLength(int newLength) {
		this.totaLength = newLength;
	}
	
	public Vector<GraphNode> getNodes() {
		return this.pathNodes;
	}

}

