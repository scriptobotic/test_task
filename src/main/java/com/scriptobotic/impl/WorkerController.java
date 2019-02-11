package com.scriptobotic.impl;

import com.scriptobotic.api.Controller;
import com.scriptobotic.api.ControllerState;
import com.scriptobotic.api.Worker;
import com.scriptobotic.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WorkerController<T extends Task> implements Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerController.class);
    private final Worker worker;
    private final ExecutorService runner = Executors.newFixedThreadPool(1);
    private volatile ControllerState state = ControllerState.INIT;

    public WorkerController(Worker worker) {
        this.worker = worker;
        LOGGER.info("{} controller for {}.", state, worker.getClass().getName());
    }

    public void start() {
        if (state == ControllerState.INIT) {
            LOGGER.info("Changing controller state for {} from {} to {}.",
                    worker.getClass().getName(), state, ControllerState.STARTED);
            runner.submit(worker::start);
            state = ControllerState.STARTED;
            LOGGER.info("Controller state for {} has been successfully changed to {}.",
                    worker.getClass().getName(), state);
        } else {
            LOGGER.warn("Controller for {} has state {}.", worker.getClass().getName(), state);
        }
    }

    public void stop() {
        LOGGER.info("Changing  controller state for {} from {} to {}.",
                worker.getClass().getName(), state, ControllerState.STOPPED);
        worker.stop();
        state = ControllerState.STOPPED;
        LOGGER.info("Controller state for {} has been successfully changed to {}.", worker.getClass().getName(), state);
    }

    public void join() {
        worker.join();
        ControllerState oldState = state;
        state = ControllerState.JOINED;
        LOGGER.info("Controller state for {} has been changed from {} to {}.",
                worker.getClass().getName(), oldState, state);
    }
}
