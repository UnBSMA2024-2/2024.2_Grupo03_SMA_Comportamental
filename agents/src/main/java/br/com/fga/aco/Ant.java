/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import java.util.Random;
import java.util.Vector;


public class Ant {
	private final ShortestACO sACO;
	private final GraphNode nullNode;
	private final Vector<GraphNode> alreadyVisited;
	private final Vector<Adjacency> arcsVisited;

	private Path currentFoundPath;
	private GraphNode currentNode;
	private GraphNode previousNode;
	private Integer numberOfRoutes;
	
	
	public Ant(ShortestACO aco) {
		this.sACO = aco;
		this.arcsVisited = new Vector<>();
		this.alreadyVisited = new Vector<>();
		this.numberOfRoutes = 0;
		this.nullNode = new GraphNode("NULL");
	}
	
	public void setStart(GraphNode newStart) {
		this.currentFoundPath = new Path();

		this.currentNode = newStart;
		this.previousNode = this.currentNode;

		this.alreadyVisited.clear();
		this.arcsVisited.clear();

		this.currentFoundPath.addGraphNode(this.currentNode);
		this.alreadyVisited.add(currentNode);
	}
	
	public boolean startTrip() {
		while (!this.currentNode.equals(this.sACO.getEndNode()) && !this.currentNode.equals(this.nullNode)) {
			this.selectNextNode();
			// System.out.println("Formiga no nó: " + this.currentNode.getName());
		}

		if (this.currentNode.equals(this.nullNode)) {
			this.currentFoundPath = new Path();
		} else {
			this.numberOfRoutes++;
			this.currentFoundPath.calculateTotaLength();
		}

		return true;
	}
	
	private void selectNextNode() {
		Random rand = new Random();

		GraphNode next = this.nullNode;
		Adjacency adj = null;
		Adjacency adj1 = null;

		double best = -0.01D;
		double current = 0.0D;

		for (Adjacency nodeAdj : this.currentNode.getAdjacents()) {
			if (!this.alreadyVisited.contains(nodeAdj.getNode())) {
				current = this.computeCoefficient(nodeAdj);

				if (current > best) {
					best = current;
					adj = nodeAdj;
					next = nodeAdj.getNode();
				} else if (current == best && rand.nextDouble() > 0.5D) {
					next = nodeAdj.getNode();
					adj = nodeAdj;
				}
			}
		}

		if (next.equals(this.nullNode)) {
			this.previousNode.deleteAdjacent(this.currentNode);
			this.currentNode.deleteAdjacent(this.previousNode);
		}

		for (Adjacency nodeAdj : next.getAdjacents()) {
			if (nodeAdj.getNode().equals(this.currentNode))
				adj1 = nodeAdj;
		}

		if (!this.arcsVisited.contains(adj)) {
			this.arcsVisited.add(adj);
			this.arcsVisited.add(adj1);
		}

		this.previousNode = this.currentNode;
		this.currentNode = next;

		if (!this.alreadyVisited.contains(currentNode))
			this.alreadyVisited.add(currentNode);

		this.currentFoundPath.addGraphNode(this.currentNode);
	}
	
	private double computeCoefficient(Adjacency info) {
		if(info.getPheromone() == ShortestACOConstants.THETA_MIN) {
			if(!this.arcsVisited.contains(info) &&  !this.alreadyVisited.contains(info.getNode()))
				return Math.pow((double)(1/info.getLength()),(double)ShortestACOConstants.ALPHA)*Math.pow((double)(1 + ShortestACOConstants.BETA),2.0D);
			else if(this.alreadyVisited.contains(info.getNode()) && !this.arcsVisited.contains(info))
				return Math.pow((1/info.getLength()),ShortestACOConstants.ALPHA)*(1 + ShortestACOConstants.BETA);
			else if(!this.alreadyVisited.contains(info.getNode()) && this.arcsVisited.contains(info))
				return Math.pow((double)(1/info.getLength()),(double)ShortestACOConstants.ALPHA)*(1 + ShortestACOConstants.BETA);
			else
				return Math.pow((double)(1/info.getLength()),(double)ShortestACOConstants.ALPHA);
		} else {
			if(this.numberOfRoutes < ShortestACOConstants.MIN_NUM_ROUTES)
				return 0.0D;
			else {
				if(!this.arcsVisited.contains(info) &&!this.alreadyVisited.contains(info.getNode()))
					return Math.pow((double)(info.getPheromone()),(double)ShortestACOConstants.ALPHA)*Math.pow((double)(1 + ShortestACOConstants.BETA),2.0D);
				else if(this.alreadyVisited.contains(info.getNode()) && !this.arcsVisited.contains(info))
					return Math.pow((double)(info.getPheromone()),(double)ShortestACOConstants.ALPHA)*(double)(1 + ShortestACOConstants.BETA);
				else if(!this.alreadyVisited.contains(info.getNode()) && this.arcsVisited.contains(info))
					return Math.pow((double)(info.getPheromone()),(double)ShortestACOConstants.ALPHA)*(double)(1 + ShortestACOConstants.BETA);
				else
					return Math.pow((double)(info.getPheromone()),(double)ShortestACOConstants.ALPHA);
			}
		}
	}
	
	public void applyPheromone() {
		if (this.currentFoundPath.getTotaLength() != 0) {
			this.sACO.setBestPath(this.currentFoundPath);
			this.currentFoundPath.releasePheromone((float) this.sACO.getBestPathLength());
		}
	}
	
}
