package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.AllDriverInfor;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class ManageDriverDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label addressView;

    @FXML
    private Label carIDView;

    @FXML
    private Label carNameView;

    @FXML
    private Label cccdView;

    @FXML
    private Label countRequest;

    @FXML
    private Label driverDateView;

    @FXML
    private Label driverEmailView;

    @FXML
    private Label driverGenderView;

    @FXML
    private Label driverIDView;

    @FXML
    private Label driverNameView;

    @FXML
    private Label driverPhoneView;

    @FXML
    private Label driverView;

    @FXML
    private ImageView img;

    @FXML
    private ImageView imgCar;

    @FXML
    private Label licenseView;

    @FXML
    private Label passView;

    @FXML
    private Label sumDistanceView;

    void SetDetail(AllDriverInfor adI){
        driverView.setText("Tài khoản: "+adI.getUser());
        passView.setText("Mật khẩu: "+adI.getPassword());
        driverNameView.setText(adI.getFullname());
        driverIDView.setText("Mã tài xế: "+adI.getDriver_id());
        driverGenderView.setText(adI.getGender());
        driverDateView.setText(adI.getDate());
        driverPhoneView.setText(adI.getPhone());
        driverEmailView.setText(adI.getEmail());
        cccdView.setText("CCCD: "+adI.getCccd());
        addressView.setText("Địa chỉ: "+adI.getAddress());
        carNameView.setText(adI.getBrand());
        carIDView.setText("Mã xe: "+adI.getId_car());
        licenseView.setText(adI.getLicense());
        countRequest.setText(String.valueOf(adI.getCount()));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(adI.getDistance());
        sumDistanceView.setText(formattedValue+" km");
    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) carNameView.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Circle clip = new Circle();
        clip.setRadius(img.getFitWidth() / 2);
        clip.setCenterX(img.getFitWidth() / 2);
        clip.setCenterY(img.getFitHeight() / 2);
        img.setClip(clip);
        Circle newClip = new Circle();
        newClip.setRadius(imgCar.getFitWidth() / 2);
        newClip.setCenterX(img.getFitWidth() / 2);
        newClip.setCenterY(img.getFitHeight() / 2);
        imgCar.setClip(newClip);
        App.setDraggable(titleView);
    }
}
