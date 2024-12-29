/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import br.com.fga.http.HttpClient;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Vector;

public class ShortestACO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3207734240578444959L;

    private final Graph graph;
    private final Vector<Ant> ants;

    @Getter
    private Path bestPath;
    private int bestPathLen;
    private int numOfPathsFound;


    public ShortestACO(Graph gr) {
        this.graph = gr;
        this.ants = new Vector<>();
        this.bestPath = new Path();
        this.bestPathLen = 0;
        this.numOfPathsFound = 0;

        for (int i = 0; i < ShortestACOConstants.NUM_ANTS; i++) {
            Ant ant = new Ant(this);
            this.ants.add(ant);
        }
    }


    public void start() {
        long startTime = System.currentTimeMillis();

        int j = 0;

        while ((j < ShortestACOConstants.MAX_ITERATIONS) && (System.currentTimeMillis() - startTime <= ShortestACOConstants.MAX_TIME_TO_WAIT)) {

            for (Ant ant : this.ants) {
                ant.setStart(this.graph.getStart());
                ant.startTrip();
                ant.applyPheromone();
            }

            this.graph.updateEdges();

            // TODO: Enviar o graph atualizado para o frontend

            j++;
        }

        if (this.bestPathLen != 0) {
            System.out.println("\n" + "Shortest Path: ");
            Vector<GraphNode> nodesOnPath = this.bestPath.getNodes();

            for (GraphNode t : nodesOnPath) {
                System.out.print(" -> " + t.getName());
            }

            System.out.println("\n\n" + "Total Distance: " + this.bestPath.getTotaLength());
        } else {
            System.out.println("\n\n" + "No path found. Maybe The node is not connected");
        }
    }

    public GraphNode getEndNode() {
        return this.graph.getEnd();
    }

    public int getBestPathLength() {
        return this.bestPathLen;
    }

    public void setBestPath(Path newPath) {
        if (this.numOfPathsFound == 0) {
            this.bestPath = newPath;
            this.bestPathLen = newPath.getTotaLength();
            this.numOfPathsFound++;
        } else {
            if (newPath.getTotaLength() < this.bestPathLen) {
                this.bestPath = newPath;
                this.bestPathLen = newPath.getTotaLength();
            }
        }
        System.out.println(newPath);
        HttpClient.post("http://localhost:8080/graph/updateNodes", newPath);
    }
}
