package com.wesolve.broker;

import java.util.HashMap;
import java.util.Map;

public class Broker {
    private Map<String, Server> servers = new HashMap<>();

    public void registerServer(String serviceType, Server server) {
        servers.put(serviceType, server);
    }

    public void routeMessage(Message message) {
        Server server = servers.get(message.getType());
        if (server != null) {
            server.handleRequest(message);
        } else {
            System.out.println("No server found for: " + message.getType());
        }
    }
}
