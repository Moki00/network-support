package com.networksupport.app;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Example list of hosts to connect to (replace with your SOHO network devices)
        List<String> hosts = Arrays.asList("192.168.1.1", "192.168.1.2");
        String username = "admin"; // Replace with your SSH username
        String password = "password"; // Replace with your SSH password or use key-based auth

        // Use virtual threads for concurrent SSH connections
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String host : hosts) {
                executor.submit(() -> connectToHost(host, username, password));
            }
            // Allow time for tasks to complete (adjust as needed)
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.error("Main thread interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private static void connectToHost(String host, String username, String password) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(username, host, 22);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // Disable for simplicity; use key-based auth in production
            session.connect();
            logger.info("Successfully connected to {}", host);
            // Perform SSH tasks (e.g., execute commands, transfer files)
            session.disconnect();
            logger.info("Disconnected from {}", host);
        } catch (JSchException e) {
            logger.error("Failed to connect to {}: {}", host, e.getMessage());
        }
    }
}