package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.TransferRequestData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DriverScrController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private ToggleButton driverAcc;

    @FXML
    private ToggleButton driverHistory;

    @FXML
    private AnchorPane driverView;

    @FXML
    private ToggleGroup gr;

    @FXML
    private ToggleButton home;

    @FXML
    private ToggleButton order;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gr.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        FXMLLoader fxmlLoader1 = new FXMLLoader(App.HelloApplication.class.getResource("home.fxml"));
        Parent root1= null;
        try {
            root1 = fxmlLoader1.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverView.getChildren().add(root1);
        FXMLLoader fxmlLoader2 = new FXMLLoader(App.HelloApplication.class.getResource("driver-booking.fxml"));
        Parent root2= null;
        try {
            root2 = fxmlLoader2.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverView.getChildren().add(root2);
        FXMLLoader fxmlLoader3 = new FXMLLoader(App.HelloApplication.class.getResource("driver-history.fxml"));
        Parent root3= null;
        try {
            root3 = fxmlLoader3.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverView.getChildren().add(root3);

        FXMLLoader fxmlLoader4 = new FXMLLoader(App.HelloApplication.class.getResource("driver-acc.fxml"));
        Parent root4= null;
        try {
            root4 = fxmlLoader4.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        driverView.getChildren().add(root4);
        TransferRequestData.requestStatusProperty().addListener(e->
        {

        });
        root1.visibleProperty().bind(home.selectedProperty());
        root2.visibleProperty().bind(order.selectedProperty());
        root3.visibleProperty().bind(driverHistory.selectedProperty());
        root4.visibleProperty().bind(driverAcc.selectedProperty());
        App.setDraggable(titleView);
    }
}
