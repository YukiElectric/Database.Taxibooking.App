package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.model.CarInfor;
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

public class ManageCarDetailController implements Initializable {

    @FXML
    private AnchorPane titleView;

    @FXML
    private Label brandView;

    @FXML
    private Label carIDView;

    @FXML
    private Label driverIDView;

    @FXML
    private Label driverNameView;

    @FXML
    private ImageView img;

    @FXML
    private ImageView imgCar;

    @FXML
    private Label lisenceView;

    @FXML
    private Label maintenanceView;

    @FXML
    private Label seatNumberView;

    @FXML
    private Label sumDistanceView;
    @FXML
    private Label noDriverInforView;
    void SetDetail(CarInfor carInfor){
        brandView.setText(carInfor.getBrand());
        carIDView.setText("Mã xe: "+carInfor.getCar_id());
        seatNumberView.setText(carInfor.getSeat_number()+" chỗ");
        maintenanceView.setText("Số lần bảo dưỡng: "+carInfor.getMaintenance());
        lisenceView.setText(carInfor.getLicense());
        if(carInfor.getFullname()==null){
            img.setVisible(false);
            driverNameView.setText("");
            driverIDView.setText("");
            noDriverInforView.setVisible(true);
        }else{
            driverNameView.setText(carInfor.getFullname());
            driverIDView.setText("Mã tài xế: "+carInfor.getDriver_id());
        }
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(carInfor.getDistance());
        sumDistanceView.setText(formattedValue+" km");

    }
    @FXML
    void goBack(ActionEvent event) {
        Stage stage = (Stage) seatNumberView.getScene().getWindow();
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
        newClip.setCenterX(imgCar.getFitWidth() / 2);
        newClip.setCenterY(imgCar.getFitHeight() / 2);
        imgCar.setClip(newClip);
        App.setDraggable(titleView);
    }
}
