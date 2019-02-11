package com.scriptobotic.impl.task;

import com.scriptobotic.api.Task;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Класс вычисляющий элемент для числа ПИ с указанной стратегией вычисления
 */
public class PiCalcTask implements Task {
    private final Consumer<Double> resultAction;
    private final Supplier<Double> strategy;
    private final double precision;

    public PiCalcTask(double precision, Consumer<Double> resultAction,
                      Supplier<Double> strategy) {
        this.precision = precision;
        this.resultAction = resultAction;
        this.strategy = strategy;
    }

    @Override
    public boolean process() {
        Double result = strategy.get();
        if (Math.abs(result) < precision) {
            return false;
        }
        resultAction.accept(result);
        return true;
    }
}
