package com.scriptobotic.impl.producer;

import com.scriptobotic.api.Task;
import com.scriptobotic.api.Worker;

import java.util.concurrent.Phaser;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * Класс для генерации {@link Task}
 * Данные будут генерироваться пока не будет вызван метод stop(), либо не достигнем Integer.MAX_VALUE
 *
 * @param <T> тип генерируемых {@link Task}
 */
public class TaskProducer<T extends Task> implements Worker {
    private final Phaser phaser = new Phaser(2);
    private final Consumer<T> sink;
    private final BiFunction<Double, Integer, T> taskSupplier;
    private volatile boolean started = false;

    public TaskProducer(Consumer<T> sink,
                        BiFunction<Double, Integer, T> taskSupplier) {
        this.sink = sink;
        this.taskSupplier = taskSupplier;
    }

    public void start() {
        started = true;
        IntStream.range(0, Integer.MAX_VALUE)
                .mapToObj(value -> taskSupplier.apply(Math.pow(-1, value), value))
                .peek(this::putTask)
                .anyMatch(t -> !started);

        phaser.arrive();
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public void join() {
        phaser.arriveAndAwaitAdvance();
    }

    private void putTask(T task) {
        sink.accept(task);
    }
}
