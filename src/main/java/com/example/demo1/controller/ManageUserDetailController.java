package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.AllUserInfor;
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

public class ManageUserDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label sumDistanceView;

    @FXML
    private Label countRequest;

    @FXML
    private Label customerDate;

    @FXML
    private Label customerEmail;

    @FXML
    private Label customerGender;

    @FXML
    private Label customerIdView;

    @FXML
    private Label customerNameView;

    @FXML
    private Label customerPhoneView;

    @FXML
    private ImageView img;

    @FXML
    private Label passView;

    @FXML
    private Label priceView;

    @FXML
    private Label userView;

    public void SetDetail(AllUserInfor auI){
        userView.setText("Tài khoản: "+auI.getUser());
        passView.setText("Mật khẩu: "+auI.getPasword());
        customerIdView.setText("Mã khách hàng: "+auI.getCustomer_id());
        customerNameView.setText(auI.getFullname());
        customerDate.setText(auI.getDate());
        customerGender.setText(auI.getGender());
        customerPhoneView.setText(auI.getPhone());
        customerEmail.setText(auI.getEmail());
        countRequest.setText(String.valueOf(auI.getCount()));
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(auI.getSum());
        sumDistanceView.setText(formattedValue+" km");
        priceView.setText(auI.getPrice());
    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) priceView.getScene().getWindow();
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
