package com.scriptobotic.api;

/**
 * Контроллер для управления состоянием процесса
 */
public interface Controller {
    /**
     * Запускает процесс
     */
    void start();

    /**
     * Останавливает процесс
     */
    void stop();

    /**
     * Ждет завершения выполнения процесса
     */
    void join();
}
