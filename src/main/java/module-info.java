module com.example.demo1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.jfoenix;
    requires de.jensd.fx.glyphs.fontawesome;
    requires google.maps.services;

    opens com.example.demo1 to javafx.fxml;
    exports com.example.demo1;
    exports com.example.demo1.controller;

    opens com.example.demo1.model to javafx.base;
    opens com.example.demo1.controller to javafx.fxml;
}