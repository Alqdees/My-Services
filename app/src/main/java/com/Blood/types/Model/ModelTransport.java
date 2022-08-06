package com.Blood.types.Model;

public class ModelTransport {
    private String name, number, type, time, from;

    public ModelTransport() {

    }

    public ModelTransport(String name, String number, String type, String time, String from) {
        this.name = name;
        this.number = number;
        this.type = type;
        this.time = time;
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}