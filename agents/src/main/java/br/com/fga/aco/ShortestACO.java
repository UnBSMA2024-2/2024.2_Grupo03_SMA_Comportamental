/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

public class ShortestACO implements Serializable {
    @Serial
    private static final long serialVersionUID = 3207734240578444959L;

    private final Graph graph;

    @Getter
    private Path bestPath;
    private int bestPathLen;
    private int numOfPathsFound;


    public ShortestACO(Graph gr) {
        this.graph = gr;
        this.bestPath = new Path();
        this.bestPathLen = 0;
        this.numOfPathsFound = 0;
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
        // System.out.println(newPath.getNodes().stream().map(it -> it.getName()).toList());
        // HttpClient.post("http://localhost:8080/graph/updateNodes", newPath);
    }
}
