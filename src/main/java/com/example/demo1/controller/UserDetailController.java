package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.UserHistory;
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

public class UserDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label arrivalView;

    @FXML
    private Label brandView;

    @FXML
    private Label departureView;

    @FXML
    private Label distanceView;

    @FXML
    private Label driverNameView;

    @FXML
    private ImageView img;

    @FXML
    private Label licenseView;

    @FXML
    private Label priceView;

    @FXML
    private Label requestIdView;

    @FXML
    private Label statusView;

    @FXML
    private Label timeView;

    public void SetDetail(UserHistory usH){
        switch (usH.getStatus()){
            case "Đã hủy": statusView.setText("Đơn đã bị hủy");break;
            case "Hoàn thành": statusView.setText("Đơn thành công");break;
            case "Thiết lập đơn": statusView.setText("Đang thiết lập đơn");break;
            case "Đã nhận đơn": statusView.setText("Tài xế đã nhận đơn");break;
            default: statusView.setText("Đơn đang được tiến hành");break;
        }
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String timeString = timeFormat.format(usH.getTime());
        String dateString = dateFormat.format(usH.getDate());
        String dateTimeString = timeString + " " + dateString;
        timeView.setText(dateTimeString);
        requestIdView.setText("Đơn hàng "+ usH.getRequest_id());
        driverNameView.setText(usH.getDriver_name());
        brandView.setText(usH.getBrand());
        licenseView.setText(usH.getLicense());
        arrivalView.setText(usH.getArrival());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(usH.getDistance());
        distanceView.setText(formattedValue+" km");
        departureView.setText(usH.getDeparture());
        priceView.setText(usH.getMoney());
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
