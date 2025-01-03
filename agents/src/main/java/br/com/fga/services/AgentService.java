package br.com.fga.services;

import jade.core.Agent;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.wrapper.AgentController;

public interface AgentService {

    AgentController createAgent(String nickname, String className, Object[] args);

    AgentController createRmaAgent(Object[] args);

    DFAgentDescription[] search(Agent agent, String serviceType);

    void register(Agent agent, DFAgentDescription dfd);

    void deregister(Agent agent);

}
