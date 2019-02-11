package com.scriptobotic.impl.consumer;

import com.scriptobotic.api.Task;
import com.scriptobotic.api.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;
import java.util.function.Supplier;

/**
 * Класс обрабатывающий {@link Task} полученную из источника
 *
 * @param <T>
 */
public class TaskConsumer<T extends Task> implements Worker {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskConsumer.class);
    private final Phaser phaser = new Phaser(2);
    private final Supplier<T> source;
    private volatile boolean started = false;

    public TaskConsumer(Supplier<T> source) {
        this.source = source;
    }

    @Override
    public void start() {
        started = true;
        T task;
        while (started && ((task = source.get()) != null && task.process())) ;
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
}
