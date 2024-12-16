package br.com.fga.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import java.io.Serial;
import java.util.Iterator;

public class AntAgent extends Agent {

    @Serial
    private static final long serialVersionUID = -6504170837111912514L;

    @Override
    protected void setup() {
        addBehaviour(new WalkBehaviour());
    }
}

class WalkBehaviour extends CyclicBehaviour {

    @Serial
    private static final long serialVersionUID = -7673518519904360075L;

    @Override
    public void action() {
        System.out.println("Walking...");
    }
}
