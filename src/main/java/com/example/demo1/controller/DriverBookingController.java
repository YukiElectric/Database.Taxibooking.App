package com.example.demo1.controller;

import com.example.demo1.daoGetUserHisoty.GetDriverHistoryDAO;
import com.example.demo1.daoRequest.SetRequestDAO;
import com.example.demo1.model.*;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class DriverBookingController implements Initializable {
    @FXML
    private HBox btn_accept;

    @FXML
    private HBox btn_booking;


    @FXML
    private JFXTextField day;

    @FXML
    private JFXTextField arrival;

    @FXML
    private JFXTextField departure;

    @FXML
    private JFXTextField hour;

    @FXML
    private JFXTextField minite;

    @FXML
    private Label money;

    @FXML
    private JFXTextField month;

    @FXML
    private JFXTextField nameClient;

    @FXML
    private VBox noOrderView;

    @FXML
    private VBox orderView;

    @FXML
    private JFXTextField phoneClient;

    @FXML
    private HBox btn_confirm;

    @FXML
    private JFXTextField year;

    static Task<Void> task;
    private boolean check=true;
    MyService service;
    void SetUpInfor(){
        DriverHistory result = GetDriverHistoryDAO.getInstance().checkStatus(TransferID.getID());
        if(result==null){
            noOrderView.toFront();
        }else {
            orderView.toFront();
            nameClient.setText(result.getCustomer_name());
            phoneClient.setText(result.getCustomer_phone());
            departure.setText(result.getDeparture());
            arrival.setText(result.getArrival());
            hour.setText(String.valueOf(result.getTime().getHours()));
            minite.setText(String.valueOf(result.getTime().getMinutes()));
            day.setText(String.valueOf(result.getDate().getDate()));
            month.setText(String.valueOf(result.getDate().getMonth()+1));
            year.setText(String.valueOf(result.getDate().getYear()+1900));
            money.setText(result.getMoney());
            if(result.getStatus().equals("Đang tiến hành")){
                btn_accept.toFront();
            }else if(result.getStatus().equals("Tiếp nhận đơn")){
                btn_confirm.toFront();
            }
        }
    }

    @FXML
    void act_booking(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Bạn có chắc chắn muốn tiếp nhận chuyến đi?");
        alert.setContentText("");
        alert.getDialogPane().setPrefSize(220, 100);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            if((SetRequestDAO.getInstance().acceptRequest(TransferID.getID()))){
                btn_confirm.toFront();
                new Service<Void>() {
                    @Override
                    protected Task<Void> createTask() {
                        // Tạo và trả về một nhiệm vụ mới
                        return new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                while (true&&check) {
                                    if(SetRequestDAO.getInstance().CheckDriverRequest(TransferID.getID())) break;
                                }
                                if (!check) return null;
                                TransferData.requestStatusProperty().setValue(!TransferData.requestStatusProperty().get());
                                return null;
                            }
                        };
                    }
                }.start();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("");
                alert1.setHeaderText("");
                alert1.setContentText("Lỗi hệ thống hoặc khách hàng đã hủy đơn");
                alert1.getDialogPane().setPrefSize(220, 100);
                alert1.showAndWait();
                noOrderView.toFront();
            }
        }
    }

    @FXML
    void act_cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Bạn có chắc chắn muốn hủy chuyến đi?");
        alert.setContentText("");
        alert.getDialogPane().setPrefSize(220, 100);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            noOrderView.toFront();
        }
    }

    @FXML
    void act_confirm(ActionEvent event) {
        SetRequestDAO.getInstance().CompleRequest(TransferID.getID());
        noOrderView.toFront();
        btn_booking.toFront();
        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
        check=true;
    }



    @FXML
    void confirmCustomer(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Xác nhận đã đón khách?");
        alert.setContentText("");
        alert.getDialogPane().setPrefSize(220, 100);
        Optional<ButtonType> option = alert.showAndWait();
        if(option.get() == ButtonType.OK){
            if(SetRequestDAO.getInstance().DriverAcceptRequest(TransferID.getID())){
                btn_accept.toFront();
                check=false;

                // if(service!=null) service.cancel();
            }else{
                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                alert1.setTitle("");
                alert1.setHeaderText("");
                alert1.setContentText("Lỗi hệ thống hoặc tài xế đã hủy đơn");
                alert1.getDialogPane().setPrefSize(220, 100);
                alert1.showAndWait();
                btn_booking.toFront();
                noOrderView.toFront();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        service = new MyService();
        SetUpInfor();
        task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
               return  null;
            }
        };
        SetRequestDAO.getInstance().ChekStatusRequest(TransferID.getID());
        TransferRequestData.requestStatusProperty().addListener(e->{
            Platform.runLater(() -> {
                SetUpInfor();
            });
        });
        TransferData.requestStatusProperty().addListener(e->{
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Khách hàng đã hủy đơn");
                alert.getDialogPane().setPrefSize(220, 100);
                alert.showAndWait();
               // if(service!=null) service.cancel();
                btn_booking.toFront();
                noOrderView.toFront();
            });
        });
    }
}

