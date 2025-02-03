package br.com.fga.controllers;

import br.com.fga.aco.Graph;
import br.com.fga.aco.GraphNode;
import br.com.fga.aco.Path;
import br.com.fga.aco.dto.GraphDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

@RestController
@RequestMapping("/graph")
public class GraphController {

    private Graph graph = Graph.INSTANCE;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<GraphDTO> getGraph() throws IOException {
        String currentPath = System.getProperty("user.dir");
        String jsonFile = currentPath + File.separator + "external-data" + File.separator + "graph.json";

        ObjectMapper mapper = new ObjectMapper();
        GraphDTO graphDTO = mapper.readValue(new File(jsonFile), new TypeReference<>() {});

        return ResponseEntity.ok().body(graphDTO);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody Graph graph) {
        simpMessagingTemplate.convertAndSend("/topic/graph/updates", graph);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/updateNodes")
    public ResponseEntity<Void> update(@RequestBody Vector<GraphNode> graphNodes) {
        simpMessagingTemplate.convertAndSend("/topic/graph/updateNodes", graphNodes);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/defineRoute")
    public ResponseEntity<Void> defineRoute(@RequestBody br.com.fga.dtos.GraphDTO graphDTO) {
        graph.defineStart(graphDTO.getInitialNode());
        graph.defineEnd(graphDTO.getFinalNode());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/defineRoute")
    public ResponseEntity<br.com.fga.dtos.GraphDTO> getDefineRoute() {
        return ResponseEntity.ok().body(new br.com.fga.dtos.GraphDTO(graph.getInitialNode(), graph.getFinalNode()));
    }

}
