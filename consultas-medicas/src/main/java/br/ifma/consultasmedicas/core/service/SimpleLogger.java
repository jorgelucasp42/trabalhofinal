package br.ifma.consultasmedicas.core.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Logger simples e estruturado para aplicação.
 * Não depende de bibliotecas externas (SLF4J/Logback).
 * Formato: [TIMESTAMP] [LEVEL] [CLASS] - MESSAGE
 */
public class SimpleLogger {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final String className;

    public SimpleLogger(Class<?> clazz) {
        this.className = clazz.getSimpleName();
    }

    public void info(String message) {
        log("INFO", message);
    }

    public void info(String message, Object... args) {
        log("INFO", String.format(message, args));
    }

    public void warn(String message) {
        log("WARN", message);
    }

    public void warn(String message, Object... args) {
        log("WARN", String.format(message, args));
    }

    public void error(String message) {
        log("ERROR", message);
    }

    public void error(String message, Throwable throwable) {
        log("ERROR", message + " | Exception: " + throwable.getMessage());
    }

    public void error(String message, Object... args) {
        log("ERROR", String.format(message, args));
    }

    public void debug(String message) {
        log("DEBUG", message);
    }

    public void debug(String message, Object... args) {
        log("DEBUG", String.format(message, args));
    }

    private void log(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logLine = String.format("[%s] [%s] [%s] - %s", timestamp, level, className, message);

        if ("ERROR".equals(level)) {
            System.err.println(logLine);
        } else {
            System.out.println(logLine);
        }
    }
}
