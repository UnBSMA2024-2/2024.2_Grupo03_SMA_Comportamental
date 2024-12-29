/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import lombok.Data;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;

@Getter
@Data
public class Path implements Serializable {
	@Serial
	private static final long serialVersionUID = 7685950879179662495L;

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

		// System.out.println(pathNodes.stream().map(it -> it.getAdjacents().stream().map(it2 -> it2.getPheromone()).toList()).toList());
		// TODO: Enviar feromonio atualizado para o frontend
		// HttpClient.post("http://localhost:8080/graph/updateNodes", pathNodes);
	}
	
	public void add(GraphNode node) {
		this.pathNodes.add(node);
	}


    public void calculateTotaLength() {
		for (int i = 0; i < this.pathNodes.size() -1; i++) {
			this.setTotaLength(this.getTotaLength() + this.pathNodes.elementAt(i).calcDistanceFromNode(this.pathNodes.elementAt(i+1)));
		}
	}

	private void setTotaLength(int newLength) {
		this.totaLength = newLength;
	}
	
	public Vector<GraphNode> getNodes() {
		return this.pathNodes;
	}

}

