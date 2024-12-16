package br.com.fga.agents;

import br.com.fga.exceptions.AgentArgsException;
import br.com.fga.http.HttpClient;
import br.com.fga.models.Ant;
import br.com.fga.services.AgentService;
import br.com.fga.services.impl.AgentServiceImpl;
import br.com.fga.utils.ThreadUtils;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.io.Serial;
import java.net.ConnectException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class AntAgent extends Agent {

    @Serial
    private static final long serialVersionUID = -6504170837111912514L;

    private Ant ant;

    private final AgentService agentService = AgentServiceImpl.getInstance();

    @Override
    protected void setup() {
        Object[] args = getArguments();

        if (args == null || args.length != 2) {
            throw new AgentArgsException("Erro no número de parâmentros.");
        }

        Integer positionX = (Integer) args[0];
        Integer positionY = (Integer) args[1];

        ant = new Ant(positionX, positionY);

        DFAgentDescription dfd = getDfAgentDescription();

        try {
            DFService.register(this, dfd);
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }

        addBehaviour(new NotifyBirthBehaviour());
        addBehaviour(new WalkBehaviour());
    }

    private DFAgentDescription getDfAgentDescription() {
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());

        ServiceDescription sdExplore = new ServiceDescription();
        sdExplore.setType("explore-map-service");
        sdExplore.setName("Explore-Map-Service");

        ServiceDescription sdPheromone = new ServiceDescription();
        sdPheromone.setType("release-pheromone-service");
        sdPheromone.setName("Release-Pheromone-Service");

        dfd.addServices(sdExplore);
        dfd.addServices(sdPheromone);
        return dfd;
    }

    @Override
    protected void takeDown() {
        try {
            DFService.deregister(this);

            // TODO: enviar via POST a lista de formigas atualizada
        } catch (FIPAException e) {
            throw new RuntimeException(e);
        }
    }

    private class NotifyBirthBehaviour extends OneShotBehaviour {

        @Serial
        private static final long serialVersionUID = -1995519278844268633L;

        @Override
        public void action() {
            DFAgentDescription[] ants = agentService.search(getAgent(), "explore-map-service");

            if (ants.length > 0) {
                List<String> agentsName = Arrays.stream(ants)
                        .map(it -> it.getName().getLocalName())
                        .toList();

                int responseCode = HttpClient.post("http://localhost:8080/ants", agentsName);

                if (responseCode != 200) {
                    ThreadUtils.sleep(5);
                    action();
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
