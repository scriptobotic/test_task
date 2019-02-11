package com.scriptobotic;

import com.scriptobotic.api.Controller;
import com.scriptobotic.api.Task;
import com.scriptobotic.api.Worker;
import com.scriptobotic.args.CLIArgs;
import com.scriptobotic.impl.consumer.TaskConsumer;
import com.scriptobotic.impl.consumer.QueueSource;
import com.scriptobotic.impl.producer.QueueSink;
import com.scriptobotic.impl.producer.TaskProducer;
import com.scriptobotic.impl.WorkerController;
import com.scriptobotic.impl.task.PiCalcTask;
import com.scriptobotic.impl.task.strategy.LeibnizStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class StandaloneSolution {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandaloneSolution.class);
    private final List<Double> results = new ArrayList<>(50);
    private final BlockingQueue<Task> taskQueue = new ArrayBlockingQueue<>(50);
    private final ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        StandaloneSolution standaloneSolution = new StandaloneSolution();
        standaloneSolution.startTaskExecutor(args);
    }

    private void startTaskExecutor(String[] args) {
        LOGGER.info("Starting task executor application.");
        CLIArgs cliArgs = CLIArgs.parse(args);
        if (cliArgs == null || cliArgs.isHelp()) {
            CLIArgs.printHelp();
            System.exit(0);
        }

        double piNumberPrecision = cliArgs.getPrecision() / Math.pow(10, cliArgs.getPrecision());
        //starting producer
        Controller producerController = getProducer(piNumberPrecision);
        producerController.start();

        //starting consumer
        Controller consumerController = getConsumer();
        consumerController.start();

        //for controllers graceful finish
        addShutDownHook(producerController::stop);
        addShutDownHook(consumerController::stop);

        //waiting for consumer to stop consuming
        consumerController.join();
        //stopping
        producerController.stop();

        //summary all collected results from consumer
        doFinalCalculation(results);
        LOGGER.info("Application has been closed.");
        System.exit(0);
    }

    private void addShutDownHook(Runnable hook) {
        Runtime.getRuntime().addShutdownHook(new Thread(hook));
    }

    private void doFinalCalculation(List<Double> results) {
        LOGGER.info("Doing final calculation.");
        Double piNumber = results.stream().reduce((aDouble, aDouble2) -> aDouble + aDouble2).orElse(0.0) * 4;
        LOGGER.info("Result of calculation is {}.", piNumber);
    }

    private Controller getProducer(double precision) {
        BiFunction<Double, Integer, Task> taskSupplier =
                (sign, iteration) -> new PiCalcTask(precision, this::resultAction, new LeibnizStrategy(sign, iteration));
        Consumer<Task> sink = new QueueSink<>(taskQueue);
        Worker producerWorker = new TaskProducer<>(sink, taskSupplier);
        return new WorkerController<>(producerWorker);
    }

    private Controller getConsumer() {
        //starting consumer
        Supplier<Task> source = new QueueSource<>(taskQueue);
        Worker consumerWorker = new TaskConsumer<>(source);
        return new WorkerController<>(consumerWorker);
    }

    /**
     * складываем полученный результат в список
     *
     * @param result результат от вычисления элемента числа ПИ
     */
    private void resultAction(Double result) {
        lock.lock();
        try {
            results.add(result);
        } finally {
            lock.unlock();
        }
    }
}
