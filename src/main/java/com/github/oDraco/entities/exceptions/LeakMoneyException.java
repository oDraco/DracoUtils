package com.github.oDraco.entities.exceptions;

public class LeakMoneyException extends RuntimeException {

    public LeakMoneyException() {
        this("Player doesn't have enough money");
    }

    public LeakMoneyException(String message) {
        super(message);
    }
}
