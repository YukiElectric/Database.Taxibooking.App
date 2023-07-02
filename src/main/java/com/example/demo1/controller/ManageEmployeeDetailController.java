package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.AllEmployeeInfor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManageEmployeeDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label addressView;

    @FXML
    private Label branchView;

    @FXML
    private Label cccdView;

    @FXML
    private Label employeeDateView;

    @FXML
    private Label employeeEmailView;

    @FXML
    private Label employeeGenderView;

    @FXML
    private Label employeeIDView;

    @FXML
    private Label employeeNameView;

    @FXML
    private Label employeePhoneView;

    @FXML
    private ImageView img;

    void SetDetail(AllEmployeeInfor aeI){
        employeeNameView.setText(aeI.getFullname());
        employeeIDView.setText("Mã nhân viên: "+aeI.getEmployee_id());
        employeeGenderView.setText(aeI.getGender());
        employeeDateView.setText(aeI.getDate());
        employeePhoneView.setText(aeI.getPhone());
        employeeEmailView.setText(aeI.getEmail());
        cccdView.setText("CCCD: "+aeI.getCccd());
        addressView.setText("Địa chỉ: "+aeI.getAddress());
        branchView.setText(aeI.getBranch());
    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) addressView.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Circle clip = new Circle();
        clip.setRadius(img.getFitWidth() / 2);
        clip.setCenterX(img.getFitWidth() / 2);
        clip.setCenterY(img.getFitHeight() / 2);
        img.setClip(clip);
        App.setDraggable(titleView);
    }
}
