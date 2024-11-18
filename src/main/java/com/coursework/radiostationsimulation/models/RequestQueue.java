package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RequestQueue {
    private ObservableList<Request> requests;

    public RequestQueue() {
        this.requests = FXCollections.observableArrayList();
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public Request processRequest() {
        if (!requests.isEmpty()) {
            return requests.remove(0); // Удаляет и возвращает первый запрос в очереди
        }
        return null;
    }

    public ObservableList<Request> getRequests() {
        return requests;
    }

    public int size() {
        return requests.size();
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }
}
