/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Vector;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GraphNode implements Serializable {

	@Serial
	private static final long serialVersionUID = 2048869609071513923L;

	private String name;
	private Vector<Adjacency> adjs;

	public GraphNode(String name) {
		this.name = name;
		this.adjs = new Vector<>();
	}

	public void addAdjacent(Adjacency adj) {
		this.adjs.add(adj);
	}

	public Vector<Adjacency> getAdjacents() {
		return this.adjs;
	}

	public int getDistanceFromNode(GraphNode node) {
		for (Adjacency t : this.adjs) {
			if (t.getNode().equals(node)) {
				return t.getLength();
			}
		}

		return -1;
	}

	public void updatePheromoneOnEdge(GraphNode node, float pheromone) {
		for (Adjacency t : this.adjs) {
			if (t.getNode().equals(node)) {
				t.addPheromone(pheromone);
				break;
			}
		}
	}

	public void updateAdjacentEdges() {
		for (Adjacency x : adjs) {
			x.evaporate();
		}
	}

	public void deleteAdjacent(GraphNode nodeToDelete) {
		for (Adjacency t : this.adjs) {
			if (t.getNode().equals(nodeToDelete)) {
				this.adjs.removeElement(t);
				break;
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		GraphNode graphNode = (GraphNode) o;
		return Objects.equals(name, graphNode.name);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(name);
	}

	@Override
	public String toString() {
		return "GraphNode{" +
				"name='" + name + '\'' +
				", adjs=" + adjs +
				'}';
	}
}

