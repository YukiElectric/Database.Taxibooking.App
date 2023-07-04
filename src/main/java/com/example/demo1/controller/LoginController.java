package com.example.demo1.controller;


import com.example.demo1.App;
import com.example.demo1.dao.AccInfoDAO;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane titleView;

    @FXML
    private Label capcha;

    private String rd;

    void SetCapcha() {
        rd = (int) (Math.random() * 90000 + 10000) + "";
        capcha.setText(rd);
    }

    void SetUser(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("homescr.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        close();
    }

    void SetDriver(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("driver-homescr.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        close();
    }

    void SetManager(){
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manager-homescr.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        close();
    }

    @FXML
    private JFXTextField confrim;

    @FXML
    private JFXPasswordField passView;

    @FXML
    private JFXTextField userView;

    @FXML
    void closeApp(MouseEvent event) {
        System.exit(0);
    }
    void close() {
        Stage stage = (Stage) capcha.getScene().getWindow();
        stage.close();
    }

    @FXML
    void forgot(MouseEvent event) {

        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("Forgot.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        close();

    }

    @FXML
    void login(ActionEvent event) {
        String cfnumber = new String(capcha.getText());
        String user = new String(userView.getText());
        String password = new String(passView.getText());
        String cf = new String(confrim.getText());
        boolean check = true;
        try {
            int ID = AccInfoDAO.getInstance().CheckUser(user);
            if (ID == 0) {
                userView.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                userView.setUnFocusColor(Color.valueOf("#0097A7"));
            }
            if (!AccInfoDAO.getInstance().CheckPass(user, password)) {
                passView.setUnFocusColor(Color.valueOf("#F04171"));
                check = false;
            } else {
                passView.setUnFocusColor(Color.valueOf("#0097A7"));
                check = true;
            }
            if (!cf.equals(cfnumber)) {
                confrim.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                confrim.setUnFocusColor(Color.valueOf("#0097A7"));
            }
            if (check && cf.equals(cfnumber)) {
                TransferID.setID(ID);
                switch (AccInfoDAO.getInstance().CheckAccountType(ID)){
                    case 1: SetManager();break;
                    case 2: SetDriver();break;
                    default: SetUser();break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        SetCapcha();
    }

    @FXML
    void minius(MouseEvent event) {
        Stage stage = (Stage) capcha.getScene().getWindow();
        stage.toBack();

    }

    @FXML
    void reset(ActionEvent event) {
        SetCapcha();
    }

    @FXML
    void sign(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("sign.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        capcha.setText((int) (Math.random() * 90000 + 10000) + "");
        confrim.setText(capcha.getText());
        App.setDraggable(titleView);
    }
}




