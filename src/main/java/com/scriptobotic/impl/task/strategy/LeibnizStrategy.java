package com.scriptobotic.impl.task.strategy;

import java.util.function.Supplier;

/**
 * Стратегия вычисления элемента для числа ПИ при помощий ряда Лейбница
 */
public class LeibnizStrategy implements Supplier<Double> {
    private Double sign;
    private Integer position;

    public LeibnizStrategy(Double sign, Integer position) {
        this.sign = sign;
        this.position = position;
    }

    @Override
    public Double get() {
        return sign / (2.0 * position + 1.0);
    }
}
