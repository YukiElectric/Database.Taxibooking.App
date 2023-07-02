package com.example.demo1.test;

import com.example.demo1.App;
import com.example.demo1.controller.CarManageController;
import javafx.fxml.FXMLLoader;

public class TestDatabase {

    public static void main(String[] args) throws InterruptedException {

        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("car-manage.fxml"));
        CarManageController controller = fxmlLoader.getController();
        System.out.println(controller);
        controller.ClearTable();
        controller.SetTable();
    }
}

