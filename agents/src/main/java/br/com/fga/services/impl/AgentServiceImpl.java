package br.com.fga.services.impl;

import br.com.fga.exceptions.AgentException;
import br.com.fga.services.AgentService;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;

import java.util.logging.Logger;

public class AgentServiceImpl implements AgentService {

    private static final AgentService INSTANCE = new AgentServiceImpl();
    private static final Logger logger = Logger.getLogger(AgentServiceImpl.class.getName());

    private ContainerController containerController;

    private AgentServiceImpl() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();
        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.MAIN_PORT, "1099");
        profile.setParameter(Profile.MAIN_HOST, "localhost");
        profile.setParameter(Profile.GUI, "false");
        try {
            containerController = runtime.createMainContainer(profile);
            System.out.println("Container criado com sucesso.");
        } catch (Exception e) {
            logger.severe("Erro ao criar o container: " + e.getMessage());
            throw new AgentException(e.getMessage());
        }
    }

    public AgentController createAgent(String nickname, String className, Object[] args) {
        try {
            return this.containerController.createNewAgent(nickname, className, args);
        } catch (StaleProxyException e) {
            logger.severe("Erro ao criar o agente: " + e.getMessage());
            throw new AgentException(e.getMessage());
        }
    }

    public AgentController createRmaAgent(Object[] args) {
        AgentController rmaAgent = null;

        try {
            // Verifica se o agente "rma" já existe
            try {
                rmaAgent = this.containerController.getAgent("rma");
                logger.info("Agente 'rma' encontrado.");
            } catch (ControllerException e) {
                logger.warning("Agente 'rma' não encontrado, criando um novo.");
                rmaAgent = this.createAgent("rma", "jade.tools.rma.rma", args);
            }
        } catch (Exception e) {
            logger.severe("Erro ao tentar criar o agente RMA: " + e.getMessage());
            throw new AgentException("Erro ao tentar criar o agente RMA: " + e.getMessage());
        }
        return rmaAgent;
    }

    public static AgentService getInstance() {
        return INSTANCE;
    }
}