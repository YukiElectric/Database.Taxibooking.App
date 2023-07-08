package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoGetCarInfor.SetCarInforDAO;
import com.example.demo1.daoSetLog.SetLogDAO;
import com.example.demo1.model.CarInfor;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class CarInforManageController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private JFXTextField brandView;

    @FXML
    private JFXRadioButton car16;

    @FXML
    private JFXRadioButton car4;

    @FXML
    private JFXRadioButton car7;

    @FXML
    private JFXRadioButton carV4;

    @FXML
    private JFXTextField driverIDView;

    @FXML
    private ToggleGroup gr;

    @FXML
    private JFXTextField licenseView;

    @FXML
    private JFXTextField maintenanceView;

    boolean status = true;
    CarInfor inputInfor = null;
    void close(){
        Stage stage = (Stage) car4.getScene().getWindow();
        stage.close();
    }

    boolean CheckNumber(String input){
        if(input==null) return true;
        try{
            Integer.parseInt(input);
            return true;
        }catch (Exception e){
            return false;
        }
    }
    boolean CheckBrand(String carName){
        String regex = "^[a-zA-Z0-9\\s]+$";
        return Pattern.matches(regex, carName);
    }
    boolean CheckLisence(String licensePlate){
        String regex = "^[0-9]{2}[A-Z]-\\d{5}$";
        return Pattern.matches(regex, licensePlate);
    }
    void SetUp(CarInfor result){
        brandView.setText(result.getBrand());
        if(result.getMaintenance()==4 && result.getBrand().equals("Vinfast")) carV4.setSelected(true);
        else switch (result.getSeat_number()){
            case 4: car4.setSelected(true);break;
            case 7: car7.setSelected(true);break;
            default: car16.setSelected(true);break;
        }
        licenseView.setText(result.getLicense());
        maintenanceView.setText(String.valueOf(result.getMaintenance()));
        driverIDView.setText(String.valueOf(result.getDriver_id()));
        status = false;
        inputInfor = result;
    }
    @FXML
    void deleteAct(ActionEvent event) {
        brandView.clear();
        car4.setSelected(true);
        licenseView.clear();
        maintenanceView.clear();
        driverIDView.clear();
    }

    int ReturnSelected(){
        if(car4.isSelected()) return 4;
        if(car7.isSelected()) return 7;
        if(car16.isSelected()) return 16;
        return 4;
    }

    @FXML
    void saveAct(ActionEvent event) {
        if (!status) {
            try {
                String newBrand = brandView.getText();
                String newLicense = licenseView.getText();
                int newSeatNumber = ReturnSelected();
                String newMaintenance = maintenanceView.getText();
                String newDriverID = driverIDView.getText();
                if (newBrand.equals(inputInfor.getBrand()) && newLicense.equals(inputInfor.getLicense()) && newSeatNumber == inputInfor.getSeat_number()
                        && Integer.parseInt(newMaintenance) == inputInfor.getMaintenance() && Integer.parseInt(newDriverID) == inputInfor.getDriver_id()) {
                    close();
                }else{
                    int check = 0;
                    if (!CheckBrand(newBrand)) {
                        brandView.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        brandView.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if (!CheckLisence(newLicense)) {
                        licenseView.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        licenseView.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    int valueMaintenance = -1;
                    if (!CheckNumber(newMaintenance)) {
                        maintenanceView.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        maintenanceView.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                        if (newMaintenance == null) {
                            valueMaintenance = 0;
                        } else {
                            valueMaintenance = Integer.parseInt(newMaintenance);
                        }
                    }
                    int vauleDriverID = -1;
                    if (Integer.parseInt(newDriverID) == inputInfor.getDriver_id()) {
                        vauleDriverID = Integer.parseInt(newDriverID);
                        maintenanceView.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    } else if (!CheckNumber(newDriverID) && !SetCarInforDAO.getInstance().CheckDriverID(Integer.parseInt(newDriverID))) {
                        driverIDView.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        maintenanceView.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                        if (newDriverID.equals("")) {
                            vauleDriverID = 0;
                        } else {
                            vauleDriverID = Integer.parseInt(newDriverID);
                        }
                    }
                    if (check == 4) {
                        if (SetCarInforDAO.getInstance().UpdateCarInfor(inputInfor.getCar_id(), newBrand, newLicense, newSeatNumber, valueMaintenance, vauleDriverID)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText("");
                            alert.setContentText("Thay đổi thành công!");
                            alert.getDialogPane().setPrefSize(220, 100);
                            alert.showAndWait();
                            close();
                            SetLogDAO.getInstance().SetRepairCarInforLog(inputInfor.getCar_id(),TransferID.getID());
                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                        } else {
                            Alert info = new Alert(Alert.AlertType.ERROR);
                            info.setTitle("");
                            info.setHeaderText("");
                            info.setContentText("Lỗi hệ thống không thể thay đổi!");
                            info.getDialogPane().setPrefSize(220, 100);
                            info.showAndWait();
                            close();
                        }
                    }
                }
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Thiếu thông tin không thể thay đổi!");
                alert.getDialogPane().setPrefSize(220, 100);
                alert.showAndWait();
            }
        }else{
            try {
                String newBrand = brandView.getText();
                String newLicense = licenseView.getText();
                int newSeatNumber = ReturnSelected();
                String newMaintenance = maintenanceView.getText();
                String newDriverID = driverIDView.getText();
                int check = 0;
                if (!CheckBrand(newBrand)) {
                    brandView.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    brandView.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (!CheckLisence(newLicense)) {
                    licenseView.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    licenseView.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                int valueMaintenance = -1;
                if (!CheckNumber(newMaintenance)) {
                    maintenanceView.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    maintenanceView.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                    if (newMaintenance == null) {
                        valueMaintenance = 0;
                    } else {
                        valueMaintenance = Integer.parseInt(newMaintenance);
                    }
                }
                int vauleDriverID = -1;
                if (!CheckNumber(newDriverID) && !SetCarInforDAO.getInstance().CheckDriverID(Integer.parseInt(newDriverID))) {
                    driverIDView.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    maintenanceView.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                    if (newDriverID == null) {
                        vauleDriverID = 0;
                    } else {
                        vauleDriverID = Integer.parseInt(newDriverID);
                    }
                }
                if(check==4){
                    if(SetCarInforDAO.getInstance().InsertCarInfor(newBrand,newLicense,newSeatNumber,valueMaintenance,vauleDriverID, TransferID.getID())){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Thêm xe thành công!");
                        alert.getDialogPane().setPrefSize(220, 100);
                        alert.showAndWait();
                        close();
                        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                    } else {
                        Alert info = new Alert(Alert.AlertType.ERROR);
                        info.setTitle("");
                        info.setHeaderText("");
                        info.setContentText("Lỗi hệ thống không thể thêm!");
                        info.getDialogPane().setPrefSize(220, 100);
                        info.showAndWait();
                    }
                }
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Thiếu thông tin không thể thêm!");
                alert.getDialogPane().setPrefSize(220, 100);
                alert.showAndWait();
            }
        }
    }

    @FXML
    void closeStage(MouseEvent event) {
        ((Stage) driverIDView.getScene().getWindow()).close();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setDraggable(titleView);
    }
}
