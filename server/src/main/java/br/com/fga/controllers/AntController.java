package br.com.fga.controllers;

import br.com.fga.models.Ant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ants")
public class AntController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping
    public ResponseEntity<Void> notifyAntBirth(@RequestBody List<Ant> ants) {
        simpMessagingTemplate.convertAndSend("/topic/ants/updates", ants);
        System.out.println("recebi");
        return ResponseEntity.ok().build();
    }

}
