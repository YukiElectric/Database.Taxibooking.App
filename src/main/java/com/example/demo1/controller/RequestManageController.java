package com.example.demo1.controller;

import com.example.demo1.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RequestManageController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private JFXButton act_booking;

    @FXML
    private JFXButton act_cancel;

    @FXML
    private JFXTextField arrival;

    @FXML
    private VBox bookingView;

    @FXML
    private VBox booking_scr;

    @FXML
    private JFXRadioButton car16;

    @FXML
    private JFXRadioButton car4;

    @FXML
    private JFXRadioButton car7;

    @FXML
    private JFXRadioButton carV4;

    @FXML
    private JFXTextField customerName;

    @FXML
    private JFXTextField day;

    @FXML
    private JFXTextField departure;

    @FXML
    private ToggleGroup gr3;

    @FXML
    private JFXTextField hour;

    @FXML
    private JFXTextField minute;

    @FXML
    private Label money;

    @FXML
    private JFXTextField month;

    @FXML
    private JFXTextField phoneView;

    @FXML
    private HBox time_select;

    @FXML
    private JFXToggleButton time_tg;

    @FXML
    private JFXTextField year;

    @FXML
    void PriceRefresh(ActionEvent event) {

    }

    @FXML
    void booking(ActionEvent event) {
        Stage stage = (Stage) day.getScene().getWindow();
        stage.close();
    }

    @FXML
    void cancel(ActionEvent event) {

    }

    public void Initializable(){
        App.setDraggable(titleView);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setDraggable(titleView);
    }
}
