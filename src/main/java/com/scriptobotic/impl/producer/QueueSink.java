package com.scriptobotic.impl.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

/**
 * Класс для передачи данных из {@link TaskProducer} основанный на {@link BlockingQueue}
 *
 * @param <T>
 */
public class QueueSink<T> implements Consumer<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(QueueSink.class);
    private BlockingQueue<T> sink;

    public QueueSink(BlockingQueue<T> sink) {
        this.sink = sink;
    }

    @Override
    public void accept(T t) {
        try {
            sink.put(t);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("Error on queuing new task.", e);
        }
    }
}
