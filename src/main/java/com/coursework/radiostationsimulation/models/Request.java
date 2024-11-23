package com.coursework.radiostationsimulation.models;

public class Request {
    public String type; // Тип запроса (исполнитель, трек, автор, альбом)
    public String value; // Значение запроса (имя исполнителя, название трека и т.д.)
    private boolean processed;

    public Request(String type, String value) {
        this.type = type;
        this.value = value;
        this.processed = false;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}
