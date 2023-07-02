package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.RequestInfor;
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

public class ManageRequestDetailController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private Label arrivalView;

    @FXML
    private Label customerIDView;

    @FXML
    private Label customerNameView;

    @FXML
    private Label customerPhoneView;

    @FXML
    private Label departureView;

    @FXML
    private Label distanceView;

    @FXML
    private Label driverIDView;

    @FXML
    private Label driverNameView;

    @FXML
    private ImageView imgCustomer;

    @FXML
    private ImageView imgDriver;

    @FXML
    private Label brandView;

    @FXML
    private Label priceView;

    @FXML
    private Label requestIdView;

    @FXML
    private Label statusView;
    @FXML
    private Label carIDView;
    @FXML
    private Label timeView;

    void SetDetail(RequestInfor result){
        switch (result.getStatus()){
            case "Đã hủy": statusView.setText("Đơn đã bị hủy");break;
            case "Hoàn thành": statusView.setText("Đơn thành công");break;
            case "Thiết lập đơn": statusView.setText("Đang thiết lập đơn");break;
            case "Đã nhận đơn": statusView.setText("Tài xế đã nhận đơn");break;
            default: statusView.setText("Đơn đang được tiến hành");break;
        }
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String timeString = timeFormat.format(result.getTime());
        String dateString = dateFormat.format(result.getDate());
        String dateTimeString = timeString + " " + dateString;
        timeView.setText(dateTimeString);
        requestIdView.setText("Đơn hàng "+ result.getId_request());
        customerNameView.setText(result.getCustomer_name());
        customerPhoneView.setText(result.getCustomer_phone());
        arrivalView.setText(result.getArrival());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(result.getDistance());
        distanceView.setText(formattedValue+" km");
        departureView.setText(result.getDeparture());
        priceView.setText(result.getPrice());
        customerIDView.setText("Mã khách hàng: "+result.getCustomer_id());
        driverIDView.setText("Mã tài xế: "+result.getDriver_id());
        driverNameView.setText(result.getDriver_name());
        carIDView.setText(result.getCar_id());
        brandView.setText(result.getBrand());
    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) timeView.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Circle clip = new Circle();
        clip.setRadius(imgCustomer.getFitWidth() / 2);
        clip.setCenterX(imgCustomer.getFitWidth() / 2);
        clip.setCenterY(imgCustomer.getFitHeight() / 2);
        imgCustomer.setClip(clip);
        Circle newClip = new Circle();
        newClip.setRadius(imgDriver.getFitWidth() / 2);
        newClip.setCenterX(imgDriver.getFitWidth() / 2);
        newClip.setCenterY(imgDriver.getFitHeight() / 2);
        imgDriver.setClip(newClip);
        App.setDraggable(titleView);
    }
}
