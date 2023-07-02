package com.example.demo1.controller;

import com.example.demo1.App;
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

public class ManagerHomeScrController implements Initializable {

    @FXML
    private AnchorPane titleView;

    @FXML
    private ToggleButton acc;

    @FXML
    private ToggleButton acc_manage;

    @FXML
    private ToggleButton car_manage;

    @FXML
    private ToggleGroup gr;

    @FXML
    private ToggleButton home;

    @FXML
    private AnchorPane scr;

    @FXML
    private ToggleButton ship_manage;

    @FXML
    private ToggleButton statictis;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setDraggable(titleView);
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
        scr.getChildren().add(root1);
        FXMLLoader fxmlLoader2 = new FXMLLoader(App.HelloApplication.class.getResource("acc-manage.fxml"));
        Parent root2= null;
        try {
            root2 = fxmlLoader2.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root2);
        FXMLLoader fxmlLoader3 = new FXMLLoader(App.HelloApplication.class.getResource("car-manage.fxml"));
        Parent root3= null;
        try {
            root3 = fxmlLoader3.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root3);

        FXMLLoader fxmlLoader4 = new FXMLLoader(App.HelloApplication.class.getResource("ship-manage.fxml"));
        Parent root4= null;
        try {
            root4 = fxmlLoader4.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root4);

        FXMLLoader fxmlLoader5 = new FXMLLoader(App.HelloApplication.class.getResource("statictis-manage.fxml"));
        Parent root5= null;
        try {
            root5 = fxmlLoader5.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root5);

        FXMLLoader fxmlLoader6 = new FXMLLoader(App.HelloApplication.class.getResource("manager-acc.fxml"));
        Parent root6= null;
        try {
            root6 = fxmlLoader6.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root6);
        root1.visibleProperty().bind(home.selectedProperty());
        root2.visibleProperty().bind(car_manage.selectedProperty());
        root3.visibleProperty().bind(acc_manage.selectedProperty());
        root4.visibleProperty().bind(ship_manage.selectedProperty());
        root5.visibleProperty().bind(statictis.selectedProperty());
        root6.visibleProperty().bind(acc.selectedProperty());
    }
}
