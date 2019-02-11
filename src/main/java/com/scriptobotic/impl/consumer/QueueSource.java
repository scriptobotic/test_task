package com.scriptobotic.impl.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.function.Supplier;

/**
 * Источник данных для {@link TaskConsumer} основанный на {@link BlockingQueue}
 *
 * @param <T> тип данных для обработки
 */
public class QueueSource<T> implements Supplier<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueSource.class);
    private BlockingQueue<T> sink;

    public QueueSource(BlockingQueue<T> sink) {
        this.sink = sink;
    }

    @Override
    public T get() {
        try {
            return sink.take();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Error when getting task from queue.", e);
            return null;
        }
    }
}
