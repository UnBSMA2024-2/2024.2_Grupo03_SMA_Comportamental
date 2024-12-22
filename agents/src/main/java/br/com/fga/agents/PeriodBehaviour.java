package br.com.fga.agents;

public enum PeriodBehaviour {

    ONE_SECOND(1_000),
    FIVE_SECONDS(ONE_SECOND.value() * 5);

    private final long period;

    PeriodBehaviour(long period) {
        this.period = period;
    }

    public long value() {
        return period;
    }

}
