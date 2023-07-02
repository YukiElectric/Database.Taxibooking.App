package com.example.demo1.controller;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class MyService extends Service<Void> {
    @Override
    protected Task<Void> createTask() {
        return DriverBookingController.task;
    }
}
