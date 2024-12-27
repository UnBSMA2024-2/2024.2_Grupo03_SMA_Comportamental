package br.com.fga.controllers;

import br.com.fga.aco.Graph;
import br.com.fga.models.Simulation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/graph")
public class GraphController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody Graph graph) {
        simpMessagingTemplate.convertAndSend("/topic/graph/updates", graph);
        return ResponseEntity.ok().build();
    }

}
