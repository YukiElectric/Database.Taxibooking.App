package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoDeleteInfor.DeleteInforDAO;
import com.example.demo1.daoGetCarInfor.GetCarInforDAO;
import com.example.demo1.model.CarInfor;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXTextField;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class CarManageController implements Initializable {
    @FXML
    private TableColumn<CarInfor, String> brand;

    @FXML
    private TableColumn<CarInfor, String> car_id;

    @FXML
    private TableColumn<CarInfor, String> driver_name;

    @FXML
    private TableColumn<CarInfor, String> license;

    @FXML
    private TableColumn<CarInfor, Integer> maintenance;

    @FXML
    private TableColumn<CarInfor, Integer> seat_number;

    @FXML
    private TableView<CarInfor> tableView;
    @FXML
    private JFXTextField searchView;

    ObservableList<CarInfor> list = FXCollections.observableArrayList();
    public void ClearTable(){
        list.clear();
    }
    public void SetTable(){
        ArrayList<CarInfor> result = GetCarInforDAO.getInstance().GetCarInfor();
        for(CarInfor rs : result){
            list.add(new CarInfor(rs.getCar_id(),rs.getLicense(),rs.getBrand(),rs.getMaintenance()
                    ,rs.getSeat_number(),rs.getDriver_id(),rs.getFullname(),rs.getDistance()));
        }
        car_id.setCellValueFactory(new PropertyValueFactory<>("car_id"));
        driver_name.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        seat_number.setCellValueFactory(new PropertyValueFactory<>("seat_number"));
        license.setCellValueFactory(new PropertyValueFactory<>("license"));
        maintenance.setCellValueFactory(new PropertyValueFactory<>("maintenance"));
        tableView.setItems(list);
        FilteredList<CarInfor> filterData = new FilteredList<>(list, b -> true);
        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(CarInfor -> {
                if(newValue.isEmpty() || newValue.isBlank() || newValue==null) return true;

                String searchKey = newValue.toLowerCase();
                if(CarInfor.getCar_id().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(CarInfor.getFullname()!=null&&CarInfor.getFullname().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(CarInfor.getBrand().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(String.valueOf(CarInfor.getSeat_number()).indexOf(searchKey) > -1){
                    return true;
                }else if(CarInfor.getLicense().toLowerCase().indexOf(searchKey) > -1){
                    return true;
                }else if(String.valueOf(CarInfor.getMaintenance()).indexOf(searchKey) > -1){
                    return true;
                }else return false;
            });
        });

        SortedList<CarInfor> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(tableView.comparatorProperty());
        tableView.setItems(sortData);
    }
    @FXML
    void addCar(ActionEvent event) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("car-infor-manage.fxml"));
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

    @FXML
    void deleteCar(ActionEvent event) {
        CarInfor selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("");
            alert.setHeaderText("Bạn có chắc chắn muốn xóa xe này không?");
            alert.setContentText("");
            alert.getDialogPane().setPrefSize(220, 100);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == ButtonType.OK) {
                if(selected.getFullname()==null){
                    DeleteInforDAO.getInstance().DeleteCarInfor(selected.getCar_id(), TransferID.getID());
                    list.remove(selected);
                }else{
                    Alert newAlert = new Alert(Alert.AlertType.INFORMATION);
                    newAlert.setTitle("");
                    newAlert.setHeaderText("");
                    newAlert.setContentText("Xe đang được sử dụng không thể xóa!");
                    newAlert.getDialogPane().setPrefSize(220, 100);
                    newAlert.showAndWait();
                }
            }
        }
        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
    }

    @FXML
    void detailCar(ActionEvent event) {
        CarInfor selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manage-car-detail.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            ManageCarDetailController controller = fxmlLoader.getController();
            controller.SetDetail(selected);
            stage.show();
        }
    }

    @FXML
    void prefixCar(ActionEvent event) {
        CarInfor selected = tableView.getSelectionModel().getSelectedItem();
        if(selected!=null){
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("car-infor-manage.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            CarInforManageController controller = fxmlLoader.getController();
            controller.SetUp(selected);
            stage.show();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SetTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            ClearTable();
            SetTable();
        });
    }
}
