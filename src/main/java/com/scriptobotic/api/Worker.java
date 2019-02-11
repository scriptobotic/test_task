package com.scriptobotic.api;

/**
 * Класс для обработки {@link Task}
 */
public interface Worker {
    /**
     * Запускает обработку
     */
    void start();

    /**
     * Завершает обрабдотку
     */
    void stop();

    /**
     * Ждет завершения текущей задачи
     */
    void join();
}
