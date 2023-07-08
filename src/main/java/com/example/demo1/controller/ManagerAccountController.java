package com.example.demo1.controller;

import com.example.demo1.App;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ManagerAccountController implements Initializable {
    @FXML
    private ImageView imgView;

    @FXML
    private Label account_type;

    @FXML
    private JFXButton btn_log_out;

    @FXML
    private JFXButton cancelBtn;

    @FXML
    private JFXButton confirmBtn;

    @FXML
    private ToggleButton editpass;

    @FXML
    private Label name;

    @FXML
    private JFXTextField newAddress;

    @FXML
    private JFXTextField newCCCD;

    @FXML
    private JFXTextField newDay;

    @FXML
    private JFXTextField newEmail;

    @FXML
    private JFXTextField newMonth;

    @FXML
    private JFXTextField newName;

    @FXML
    private JFXPasswordField newPass;

    @FXML
    private JFXTextField newPhone;

    @FXML
    private JFXTextField newYear;

    @FXML
    private JFXPasswordField reNewPass;

    @FXML
    private HBox repassView;

    @FXML
    void cancel(ActionEvent event) {
        editpass.setSelected(false);
    }

    @FXML
    void confirm(ActionEvent event) {
        editpass.setSelected(false);
    }

    @FXML
    void setPass(ActionEvent event) {
        if (editpass.isSelected()) {
            newPass.clear();
        } else newPass.setText("********");
    }
    @FXML
    void logOut(ActionEvent event) {
        Stage st = (Stage) btn_log_out.getScene().getWindow();
        st.close();
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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        repassView.visibleProperty().bind(editpass.selectedProperty());
        cancelBtn.visibleProperty().bind(editpass.selectedProperty());
        confirmBtn.visibleProperty().bind(editpass.selectedProperty());
        Circle clip = new Circle();
        clip.setRadius(imgView.getFitWidth() / 2);
        clip.setCenterX(imgView.getFitWidth() / 2);
        clip.setCenterY(imgView.getFitHeight() / 2);
        imgView.setClip(clip);
        newPass.setText("********");
    }
}
