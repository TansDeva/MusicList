package com.tanshul.player.web;

/**
 * Created by tansdeva on 20/12/17.
 * Default server response class
 */
public class Response {
    private String data;

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ClassPojo [data = " + data + "]";
    }
}