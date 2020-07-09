package com.example.nonprofitapp;

import android.app.Application;

public class MyApplication extends Application {
    private Model model;

    public MyApplication() {
        model = new Model();
    }

    public Model getModel() {
        return model;
    }
}
