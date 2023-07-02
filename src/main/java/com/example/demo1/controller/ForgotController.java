package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoResetPass.UserResetDAO;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ForgotController {
    private String SetSendNumber(){
        return (int) (Math.random()*900000+100000)+"";
    }

    String sendNumber;
    int ID;

    @FXML
    private AnchorPane titleView;

    @FXML
    private JFXPasswordField code;

    @FXML
    private JFXTextField email;

    @FXML
    private JFXPasswordField newpass;

    @FXML
    private JFXPasswordField repass;
    @FXML
    private HBox scr;

    @FXML
    void closeApp(MouseEvent event) {
        System.exit(0);
    }
    void close() {
        Stage stage = (Stage) code.getScene().getWindow();
        stage.close();
    }

    @FXML
    void confirm(ActionEvent event) {
        String userEmail = new String(email.getText());
        String confirmCode = new String(code.getText());
        int check = 0;
        try{
            if(UserResetDAO.getInstance().CheckEmail(userEmail)==0){
                email.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                email.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!confirmCode.equals(sendNumber)){
                code.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                code.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(check==2){
                ID = UserResetDAO.getInstance().CheckEmail(userEmail);
                scr.setTranslateX(-370);
            }
        }catch (Exception e){

        }
    }

    @FXML
    void login(MouseEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("login.fxml"));
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
    void minius(MouseEvent event) {
        Stage stage = (Stage) code.getScene().getWindow();
        stage.toBack();
    }

    @FXML
    void reset(ActionEvent event) {
        String newPass = new String(newpass.getText());
        String rePass = new String(repass.getText());
        int check = 0;
        try {
            if(newPass.length()<8){
                newpass.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                newpass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!rePass.equals(newPass)){
                repass.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                repass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(check==2 && UserResetDAO.getInstance().ResetPass(ID,newPass)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Đổi mật khẩu thành công");
                alert.getDialogPane().setPrefSize(220,150);
                alert.showAndWait();
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("login.fxml"));
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
            }else{
                newpass.setUnFocusColor(Color.valueOf("#F04171"));
                repass.setUnFocusColor(Color.valueOf("#F04171"));
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Mật khẩu mới phải khác mật khẩu cũ và ít nhất 8 ký tự");
                alert.getDialogPane().setPrefSize(220,150);
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void send(ActionEvent event) {
        sendNumber = SetSendNumber();
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Mã xác nhận của bạn là: "+sendNumber);
        alert.getDialogPane().setPrefSize(220,100);
        alert.showAndWait();
    }

    public void initialize() {
        App.setDraggable(titleView);
    }
}
