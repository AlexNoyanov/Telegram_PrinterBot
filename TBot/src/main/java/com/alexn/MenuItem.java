package com.alexn;

public class MenuItem {
    private String name;
    private String action;

    public MenuItem(String s, String prevAction) {
        name = s;
        action = prevAction;
    }

    public String getName() {
        return name;
    }

    public String getAction() {
        return action;
    }

    // getters, setters here
    // all args constructor here
}
