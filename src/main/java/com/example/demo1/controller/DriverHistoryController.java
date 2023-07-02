package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoGetUserHisoty.GetDriverHistoryDAO;
import com.example.demo1.model.DriverHistory;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DriverHistoryController implements Initializable {
    @FXML
    private TableColumn<DriverHistory, String> arrivalTxt;

    @FXML
    private TableColumn<DriverHistory, String> departureTxt;

    @FXML
    private TableColumn<DriverHistory, String> idRequestTxt;

    @FXML
    private TableColumn<DriverHistory, String> priceTxt;

    @FXML
    private TableColumn<DriverHistory, String> statusTxt;

    @FXML
    private TableView<DriverHistory> tableView;

    @FXML
    private TableColumn<DriverHistory, String> timeTxt;

    @FXML
    void detailOrder(ActionEvent event) {
        DriverHistory selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("driver-detail.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            DriverDetailController controller = fxmlLoader.getController();
            controller.SetDetail(selected);
            stage.show();
        }
    }
    ObservableList<DriverHistory> list = FXCollections.observableArrayList();
    void SetTable(){
        ArrayList<DriverHistory> driverHistory = GetDriverHistoryDAO.getInstance().getHistory(TransferID.getID());
        for(DriverHistory drH : driverHistory){
            list.add(new DriverHistory(drH.getRequest_id(), drH.getTime(),drH.getDate(),drH.getDeparture(),drH.getArrival()
                    ,drH.getMoney(),drH.getDistance(),drH.getStatus(),drH.getCustomer_name(),drH.getCustomer_phone()));
        }
        idRequestTxt.setCellValueFactory(new PropertyValueFactory<DriverHistory, String>("request_id"));
        departureTxt.setCellValueFactory(new PropertyValueFactory<DriverHistory, String>("departure"));
        arrivalTxt.setCellValueFactory(new PropertyValueFactory<DriverHistory, String>("arrival"));
        priceTxt.setCellValueFactory(new PropertyValueFactory<DriverHistory, String>("money"));
        statusTxt.setCellValueFactory(new PropertyValueFactory<DriverHistory, String>("status"));
        timeTxt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DriverHistory, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DriverHistory, String> param) {
                DriverHistory data = param.getValue();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String timeString = timeFormat.format(data.getTime());
                String dateString = dateFormat.format(data.getDate());
                String dateTimeString = timeString + " " + dateString;
                return new SimpleStringProperty(dateTimeString);
            }
        });
        tableView.setItems(list);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            list.clear();
            SetTable();
        });
    }
}
