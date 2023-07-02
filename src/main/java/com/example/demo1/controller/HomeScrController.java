package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.TransferData;
import com.example.demo1.model.TransferVoucher;
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

public class HomeScrController implements Initializable{
    @FXML
    private AnchorPane titleView;

    @FXML
    private ToggleGroup gr;

    @FXML
    private AnchorPane scr;

    @FXML
    private ToggleButton act;


    @FXML
    private ToggleButton home;

    @FXML
    private ToggleButton vou;
    @FXML
    private ToggleButton acc;


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
        scr.getChildren().add(root1);
        FXMLLoader fxmlLoader2 = new FXMLLoader(App.HelloApplication.class.getResource("activity.fxml"));
        Parent root2= null;
        try {
            root2 = fxmlLoader2.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root2);
        FXMLLoader fxmlLoader3 = new FXMLLoader(App.HelloApplication.class.getResource("voucher.fxml"));
        Parent root3= null;
        try {
            root3 = fxmlLoader3.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root3);

        FXMLLoader fxmlLoader4 = new FXMLLoader(App.HelloApplication.class.getResource("account.fxml"));
        Parent root4= null;
        try {
            root4 = fxmlLoader4.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        scr.getChildren().add(root4);

        root1.visibleProperty().bind(home.selectedProperty());
        root2.visibleProperty().bind(act.selectedProperty());
        root3.visibleProperty().bind(vou.selectedProperty());
        root4.visibleProperty().bind(acc.selectedProperty());
        TransferData.statusProperty().addListener((observable, oldVal, newVal)->{
            act.setSelected(true);
        });
        TransferVoucher.statusProperty().addListener((observable, oldValue, newValue) -> {
            vou.setSelected(true);
        });
        App.setDraggable(titleView);
    }
}
