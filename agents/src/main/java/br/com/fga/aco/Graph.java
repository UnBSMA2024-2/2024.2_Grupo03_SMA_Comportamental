/*
 * @autor MicheleZito
 * @link https://github.com/MicheleZito
 * @version 1.0
 * @adaptation: @DouglasMonteles
 * */

package br.com.fga.aco;

import br.com.fga.aco.dto.GraphDTO;
import br.com.fga.aco.dto.NodeAdjacentDTO;
import br.com.fga.aco.dto.NodeDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.System.exit;

@Data
public class Graph {

    private final Vector<GraphNode> nodes;

    private GraphNode start;
	private GraphNode end;

	public Graph() {
		this.nodes = new Vector<>();
		this.start = new GraphNode(null);
		this.end = new GraphNode(null);
	}

	public void updateEdges() {
		for (GraphNode t : nodes)
			t.updateAdjacentEdges();
	}


    public void setStart(String name) {
        boolean cond = FALSE;

        for(GraphNode t : this.nodes) {
            if (t.getName().equals(name)) {
                this.start = t;
                cond = TRUE;
                break;
		    }
        }

        if (!cond) {
            System.out.println("\n\n" + "[!!] Error : Insert name of existing node");
            exit(1);
        }
	}

	
	public void setEnd(String name) {
        boolean  cond = FALSE;

        for (GraphNode t : this.nodes) {
            if (t.getName().equals(name)) {
                this.end = t;
                cond = TRUE;
                break;
            }
        }

        if (!cond) {
            System.out.println("\n\n" + "[!!] Error : Insert name of existing node");
            exit(1);
        }
	}
	
	
	public void buildGraph() {
        String currentPath = System.getProperty("user.dir");
        String jsonFile = currentPath + File.separator + "external-data" + File.separator + "graph.json";

        ObjectMapper mapper = new ObjectMapper();

        try {
            GraphDTO graphDTO = mapper.readValue(new File(jsonFile), new TypeReference<>() {});

            graphDTO.getNodes().forEach(node -> this.nodes.add(new GraphNode(node.getNode())));

            for (int i = 0; i < graphDTO.getNodes().size(); i++) {
                NodeDTO node = graphDTO.getNodes().get(i);

                for (NodeAdjacentDTO nodeEdge : node.getEdges()) {
                    String nodeName = nodeEdge.getNode();
                    Integer distance = nodeEdge.getDistance();

                    for (GraphNode graphNode : this.nodes) {
                        if (graphNode.getName().equals(nodeName)) {
                            this.nodes.elementAt(i).addAdjacent(new Adjacency(graphNode, distance));
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}

        
    public void printGraph() {
        System.out.println("\n"+"Printing Graph" + "\n");

        for (GraphNode x : this.nodes) {
            System.out.println("\n"+ "Node name:  " +  x.getName());
            System.out.println("Adjacencies:");

            for (Adjacency t : x.getAdjacents()) {
                System.out.println(t.getNode().getName() + " with distance: " + t.getLength());
            }
        }
    }
}
