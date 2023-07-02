package com.example.demo1.controller;

import com.example.demo1.dao.AccInfoDAO;
import com.example.demo1.daoGetCarInfor.GetCarInforDAO;
import com.example.demo1.daoGetSalary.GetSalaryDAO;
import com.example.demo1.daoRequest.GetRequestDAO;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.StatictisModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class StatictisManageController implements Initializable{
    @FXML
    private Label cancelShipNumberSet;

    @FXML
    private Label carSet;

    @FXML
    private Label completeShipNumberSet;

    @FXML
    private TableColumn<StatictisModel, String> date;

    @FXML
    private Label driverSet;

    @FXML
    private TableColumn<StatictisModel, String> earning;

    @FXML
    private Label employeeSet;

    @FXML
    private TableColumn<StatictisModel, String> salary;

    @FXML
    private ComboBox<String> selectSet;

    @FXML
    private Label shipNumberSet;

    @FXML
    private TableColumn<StatictisModel, String> supplementary;

    @FXML
    private TableView<StatictisModel> tableView;

    @FXML
    private Label userSet;

    @FXML
    void setCarNumber(ActionEvent event) {
        switch (selectSet.getSelectionModel().getSelectedItem()){
            case "Tổng số":  carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(0)));break;
            case "4 chỗ": carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(1)));break;
            case "Vinfast 4 chỗ": carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(2)));break;
            case "7 chỗ": carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(3)));break;
            default: carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(4)));break;
        }
    }
    ObservableList<StatictisModel> list = FXCollections.observableArrayList();
    public void SetStatictisInfor(){
        userSet.setText(String.valueOf(AccInfoDAO.getInstance().GetCountAcc(0)));
        driverSet.setText(String.valueOf(AccInfoDAO.getInstance().GetCountAcc(1)));
        employeeSet.setText(String.valueOf(AccInfoDAO.getInstance().GetCountAcc(2)));
        shipNumberSet.setText(String.valueOf(GetRequestDAO.getInstance().GetCountRequest(0)));
        completeShipNumberSet.setText(String.valueOf(GetRequestDAO.getInstance().GetCountRequest(1)));
        cancelShipNumberSet.setText(String.valueOf(GetRequestDAO.getInstance().GetCountRequest(2)));
        carSet.setText(String.valueOf(GetCarInforDAO.getInstance().GetCountCar(0)));
    }
    void SetStaticsTableInfor(){
        ArrayList<StatictisModel> result = GetSalaryDAO.getInstance().GetSalary();
        for(StatictisModel rs : result){
            list.add(new StatictisModel(rs.getDate(), rs.getSupplementary(), rs.getSalary(),rs.getEarning()));
        }
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        supplementary.setCellValueFactory(new PropertyValueFactory<>("supplementary"));
        salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        earning.setCellValueFactory(new PropertyValueFactory<>("earning"));
        tableView.setItems(list);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectSet.setItems(FXCollections.observableArrayList("Tổng số","4 chỗ","Vinfast 4 chỗ","7 chỗ","16 chỗ"));
        selectSet.getSelectionModel().select(0);
        SetStatictisInfor();
        SetStaticsTableInfor();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            SetStatictisInfor();
        });
    }
}
