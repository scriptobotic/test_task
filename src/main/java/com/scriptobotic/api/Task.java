package com.scriptobotic.api;

/**
 * Класс для выполнения задач
 */
public interface Task {
    /**
     * Запускает задачу на выполнение
     *
     * @return возвращает true, если необходимо продолжить обработку задач в {@link Worker}
     */
    boolean process();
}
