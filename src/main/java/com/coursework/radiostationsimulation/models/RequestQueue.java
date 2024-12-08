package com.coursework.radiostationsimulation.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RequestQueue {
    private ObservableList<Request> requests;

    // Конструктор
    public RequestQueue() {
        this.requests = FXCollections.observableArrayList();
    }

    // Добавление запроса
    public void addRequest(Request request) {
        requests.add(request);
    }

    // Обработка запроса
    public Request processRequest() {
        if (!requests.isEmpty()) {
            return requests.remove(0); // Удаляет и возвращает первый запрос в очереди
        }
        return null;
    }

    // Метод для удаления конкретного запроса (по объекту)
    public boolean removeRequest(Request request) {
        return requests.remove(request); // Удаляет запрос из очереди
    }

    public ObservableList<Request> getRequests() {
        return requests;
    }

    // Сброс запросов
    public void clear() {
        requests.clear(); // Очистка списка запросов
    }

    public int size() {
        return requests.size();
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }
}
