package br.com.fga.controllers;

import br.com.fga.mock.SimulatorData;
import br.com.fga.models.Simulation;
import br.com.fga.models.Truck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/update")
    public ResponseEntity<Void> update(@RequestBody List<Simulation> simulatorData) {
        simpMessagingTemplate.convertAndSend("/topic/simulation/update", simulatorData);
        return ResponseEntity.ok().build();
    }

}
