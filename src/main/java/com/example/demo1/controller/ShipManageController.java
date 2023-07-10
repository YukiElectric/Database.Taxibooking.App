package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoDeleteInfor.DeleteInforDAO;
import com.example.demo1.daoRequest.GetRequestDAO;
import com.example.demo1.model.RequestInfor;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
import java.util.ResourceBundle;

public class ShipManageController implements Initializable {
    @FXML
    private TableColumn<RequestInfor, String> arrival;

    @FXML
    private TableColumn<RequestInfor, String> departure;

    @FXML
    private TableColumn<RequestInfor, String> driver;

    @FXML
    private TableColumn<RequestInfor, String> id_request;

    @FXML
    private JFXTextField searchView;

    @FXML
    private TableColumn<RequestInfor, String> status;

    @FXML
    private TableView<RequestInfor> tableView;

    @FXML
    private TableColumn<RequestInfor, String> time;


    @FXML
    void deleteShip(ActionEvent event) {
        RequestInfor selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa đơn này không?");
            alert.setContentText("");
            alert.getDialogPane().setPrefSize(220, 100);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                DeleteInforDAO.getInstance().DeleteRequestInfor(selected.getId_request(), TransferID.getID());
                list.remove(selected);
                RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
            }
        }
    }

    @FXML
    void detailShip(ActionEvent event) {
        RequestInfor selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manage-request-detail.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            ManageRequestDetailController controller = fxmlLoader.getController();
            controller.SetDetail(selected);
            stage.show();
        }
    }

    @FXML
    void addShip(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("request-manage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }
    ObservableList<RequestInfor> list = FXCollections.observableArrayList();
    void SetTable(){
        ArrayList<RequestInfor> result = GetRequestDAO.getInstance().GetRequestInfor();
        for(RequestInfor rs : result){
            list.add(new RequestInfor(rs.getId_request(),rs.getDeparture(),rs.getTime(),rs.getDate(),rs.getArrival(),rs.getPrice(),rs.getDriver_id(),
                    rs.getCustomer_id(),rs.getDistance(),rs.getStatus(),rs.getCar_id(),rs.getDriver_name(),rs.getCustomer_name(),rs.getBrand(), rs.getCustomer_phone()));
        }
        id_request.setCellValueFactory(new PropertyValueFactory<>("id_request"));
        departure.setCellValueFactory(new PropertyValueFactory<>("departure"));
        arrival.setCellValueFactory(new PropertyValueFactory<>("arrival"));
        time.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<RequestInfor, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<RequestInfor, String> param) {
                RequestInfor data = param.getValue();
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String timeString = timeFormat.format(data.getTime());
                String dateString = dateFormat.format(data.getDate());
                String dateTimeString = timeString + " " + dateString;
                return new SimpleStringProperty(dateTimeString);
            }
        });
        driver.setCellValueFactory(new PropertyValueFactory<>("driver_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        tableView.setItems(list);
        FilteredList<RequestInfor> filterData = new FilteredList<>(list, b -> true);
        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(RequestInfor -> {
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String timeString = timeFormat.format(RequestInfor.getTime());
                String dateString = dateFormat.format(RequestInfor.getDate());
                String dateTimeString = timeString + " " + dateString;
                if(newValue.isEmpty() || newValue.isBlank() || newValue==null) return true;

                String searchKey = newValue.toLowerCase();
                if(RequestInfor.getId_request().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(RequestInfor.getDeparture().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(RequestInfor.getArrival().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(dateTimeString.indexOf(searchKey) > -1){
                    return true;
                }else if(RequestInfor.getDriver_name()!=null && RequestInfor.getDriver_name().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(RequestInfor.getStatus().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else return false;
            });
        });

        SortedList<RequestInfor> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortData);
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
