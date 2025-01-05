package br.com.fga.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/agents")
    public ResponseEntity<Void> notifyAntBirth(@RequestBody List<String> ants) {
        simpMessagingTemplate.convertAndSend("/topic/vehicles/agents/alive", ants);
        return ResponseEntity.ok().build();
    }

}
