package br.com.fga.controllers;

import br.com.fga.mock.SimulatorData;
import br.com.fga.models.Simulation;
import br.com.fga.models.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping
    public ResponseEntity<List<Simulation>> vehicles() {
        simpMessagingTemplate.convertAndSend("/topic/updates", "{\"message\": \"teste\"}");
        return ResponseEntity.ok().body(SimulatorData.SIMULATION_LIST);
    }

    @MessageMapping("/hello")
    @SendTo("/topic/updates")
    public String greeting() throws Exception {
        Thread.sleep(1000); // simulated delay
        return "Hello, " + HtmlUtils.htmlEscape("teste") + "!";
    }

}
