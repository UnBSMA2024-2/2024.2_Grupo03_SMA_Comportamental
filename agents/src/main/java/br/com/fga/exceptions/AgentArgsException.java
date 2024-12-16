package br.com.fga.exceptions;

import java.io.Serial;

public class AgentArgsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7175721164596022358L;

    public AgentArgsException(String msg) {
        super(msg);
    }

}
