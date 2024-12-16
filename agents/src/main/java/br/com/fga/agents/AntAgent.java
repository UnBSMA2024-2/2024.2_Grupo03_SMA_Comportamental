package br.com.fga.agents;

import br.com.fga.http.HttpClient;
import br.com.fga.models.Ant;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import java.io.Serial;
import java.net.ConnectException;
import java.util.Iterator;
import java.util.List;

public class AntAgent extends Agent {

    @Serial
    private static final long serialVersionUID = -6504170837111912514L;

    private Ant ant;

    @Override
    protected void setup() {
        ant = new Ant(10, 10);

        addBehaviour(new NotifyBirthBehaviour());
        addBehaviour(new WalkBehaviour());
    }

    private class NotifyBirthBehaviour extends OneShotBehaviour {

        @Serial
        private static final long serialVersionUID = -1995519278844268633L;

        @Override
        public void action() {
            int responseCode = HttpClient.post("http://localhost:8080/ants", List.of(ant));

            if (responseCode != 200) {
                try {
                    Thread.sleep(1000);
                    action();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    private class WalkBehaviour extends CyclicBehaviour {

        @Serial
        private static final long serialVersionUID = -7673518519904360075L;

        @Override
        public void action() {
            ant.setPositionX(ant.getPositionX() + 1);
            ant.setPositionY(ant.getPositionY() + 1);

            System.out.println("Walking...");
        }
    }
}
