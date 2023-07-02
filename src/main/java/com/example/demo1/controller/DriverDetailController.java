package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.DriverHistory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class DriverDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label arrivalView;

    @FXML
    private Label customerNameView;

    @FXML
    private Label customerPhoneView;

    @FXML
    private Label departureView;

    @FXML
    private Label distanceView;

    @FXML
    private ImageView img;

    @FXML
    private Label priceView;

    @FXML
    private Label requestIdView;

    @FXML
    private Label statusView;

    @FXML
    private Label timeView;
    public void SetDetail(DriverHistory dH){
        switch (dH.getStatus()){
            case "Đã hủy": statusView.setText("Đơn đã bị hủy");break;
            case "Hoàn thành": statusView.setText("Đơn thành công");break;
            case "Thiết lập đơn": statusView.setText("Đang thiết lập đơn");break;
            case "Đã nhận đơn": statusView.setText("Tài xế đã nhận đơn");break;
            default: statusView.setText("Đơn đang được tiến hành");break;
        }
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String timeString = timeFormat.format(dH.getTime());
        String dateString = dateFormat.format(dH.getDate());
        String dateTimeString = timeString + " " + dateString;
        timeView.setText(dateTimeString);
        requestIdView.setText("Đơn hàng "+ dH.getRequest_id());
        customerNameView.setText(dH.getCustomer_name());
        customerPhoneView.setText(dH.getCustomer_phone());
        arrivalView.setText(dH.getArrival());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(dH.getDistance());
        distanceView.setText(formattedValue+" km");
        departureView.setText(dH.getDeparture());
        priceView.setText(dH.getMoney());
    }

    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) timeView.getScene().getWindow();
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
