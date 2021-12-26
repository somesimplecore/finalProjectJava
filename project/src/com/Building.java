package com;

import java.util.ArrayList;

public class Building {
    private ArrayList<String> allValues;

    public Building (ArrayList<String> values) {
        this.allValues = values;
    }

    public ArrayList<String> getAllValues() {
        return  allValues;
    }
}
