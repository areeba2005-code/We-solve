package com.wesolve.broker;

public class Client {
    private Broker broker;

    public Client(Broker broker) {
        this.broker = broker;
    }

    public void sendRequest(Message message) {
        broker.routeMessage(message);
    }
}
