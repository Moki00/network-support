package com.networksupport.app;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    String[] hosts = { "host1.example.com", "host2.example.com" }; // Add your hostnames here
    try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
        for (String host : hosts) {
            executor.submit(() -> connectToHost(host));
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    private static void connectToHost(String host) {
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession("user", host, 22);
            session.setPassword("password");
            session.setConfig("StrictHostKeyChecking", "no");
            session.connect();
            System.out.println("Connected to " + host);
            session.disconnect();
        } catch (Exception e) {
            System.err.println("Failed to connect to " + host + ": " + e.getMessage());
        }
    }
}