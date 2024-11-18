package com.coursework.radiostationsimulation.models;

public class Request {
    public String type; // Тип запроса (исполнитель, трек, автор, альбом)
    public String value; // Значение запроса (имя исполнителя, название трека и т.д.)

    public Request(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
