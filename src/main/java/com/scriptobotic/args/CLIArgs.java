package com.scriptobotic.args;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Интерфейс командной строки
 */
public class CLIArgs {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIArgs.class);
    @Parameter(names = {"-p", "--precision"}, description = "pi number precision as int value")
    private int precision = 1;

    @Parameter(names = "--help", description = "print CLI help", help = true)
    private boolean help;

    /**
     * Парсит переданные значения команднйо строки в {@link CLIArgs}
     *
     * @param argv список аргументов команднйо строки
     * @return структуру с параметрами запуска
     */
    public static CLIArgs parse(String... argv) {
        CLIArgs cliArgs = new CLIArgs();
        try {
            new JCommander(cliArgs).parse(argv);
            return cliArgs;
        } catch (RuntimeException e) {
            LOGGER.error("Error parsing CLI parameters.", e);
            return null;
        }
    }

    /**
     * Выводит в консоль подсказу по командной строке
     */
    public static void printHelp() {
        new JCommander(new CLIArgs()).usage();
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }
}
