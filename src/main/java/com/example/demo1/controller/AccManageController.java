package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoDeleteInfor.DeleteInforDAO;
import com.example.demo1.daoGetUserInfo.DAOGetAllUserInfor;
import com.example.demo1.model.*;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class AccManageController implements Initializable {

    @FXML
    private ComboBox<String> AccSelect;

    @FXML
    private TableColumn<AllDriverInfor, String> DoBTxt;

    @FXML
    private TableColumn<AllDriverInfor, Integer> countDriverRequestTxt;

    @FXML
    private TableColumn<String, Integer> countRequestTxt;

    @FXML
    private TableColumn<AllUserInfor, Integer> customerIDTxt;

    @FXML
    private TableColumn<AllUserInfor, String> customerNametxt;

    @FXML
    private TableColumn<AllUserInfor, String> customerPhoneTxt;

    @FXML
    private TableView<AllUserInfor> customerTableView;

    @FXML
    private AnchorPane driverAccView;

    @FXML
    private TableColumn<AllDriverInfor, String> driverCarIDTxt;

    @FXML
    private TableColumn<AllDriverInfor, Integer> driverIDTxt;

    @FXML
    private TableColumn<AllDriverInfor, String> driverNameTxt;

    @FXML
    private TableColumn<AllDriverInfor, String> driverPhoneTxt;

    @FXML
    private TableView<AllDriverInfor> driverTableView;

    @FXML
    private TableColumn<AllEmployeeInfor, String> employeeDoBTxt;

    @FXML
    private TableColumn<AllEmployeeInfor, String> employeeIDTxt;

    @FXML
    private TableColumn<AllEmployeeInfor, String> employeeNametxt;

    @FXML
    private TableColumn<AllEmployeeInfor, String> employeeOfficeTxt;

    @FXML
    private TableView<AllEmployeeInfor> employeeTableView;

    @FXML
    private AnchorPane managerAccView;

    @FXML
    private JFXTextField searchView;

    @FXML
    private VBox userAccView;

    @FXML
    void addAcc(ActionEvent event) {
        if (AccSelect.getSelectionModel().getSelectedItem().equals("Khách hàng")) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("user-manage.fxml"));
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
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Tài xế")) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("driver-manage.fxml"));
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
        } else if(AccSelect.getSelectionModel().getSelectedItem().equals("Nhân viên")){
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("employee-manage.fxml"));
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
    }

    @FXML
    void deleteAcc(ActionEvent event) {
        if (AccSelect.getSelectionModel().getSelectedItem().equals("Khách hàng")) {
            AllUserInfor selected = customerTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Bạn có chắc chắn xóa khách hàng này không?");
                alert.setContentText("");
                alert.getDialogPane().setPrefSize(220, 100);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    DeleteInforDAO.getInstance().DeleteCustomerInfor(selected.getCustomer_id(), TransferID.getID());
                    UserList.remove(selected);
                }
            }
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Tài xế")) {
            AllDriverInfor selected = driverTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Bạn có chắc chắn muốn xóa tài xế này không?");
                alert.setContentText("");
                alert.getDialogPane().setPrefSize(220, 100);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    DeleteInforDAO.getInstance().DeleteDriverInfor(selected.getDriver_id(), TransferID.getID());
                    DriverList.remove(selected);
                }
            }
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Nhân viên")) {
            AllEmployeeInfor selected = employeeTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("");
                alert.setHeaderText("Bạn có chắc chắn muốn xóa nhân viên này không?");
                alert.setContentText("");
                alert.getDialogPane().setPrefSize(220, 100);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get() == ButtonType.OK) {
                    DeleteInforDAO.getInstance().DeleteEmployeeInfor(selected.getEmployee_id(), TransferID.getID());
                    EmployeeList.remove(selected);
                }
            }
        }
        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
    }

    @FXML
    void detailAcc(ActionEvent event) {
        if (AccSelect.getSelectionModel().getSelectedItem().equals("Khách hàng")) {
            AllUserInfor selected = customerTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manage-user-detail.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                ManageUserDetailController controller = fxmlLoader.getController();
                controller.SetDetail(selected);
                stage.show();
            }
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Tài xế")) {
            AllDriverInfor selected = driverTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manage-driver-detail.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                ManageDriverDetailController controller = fxmlLoader.getController();
                controller.SetDetail(selected);
                stage.show();
            }
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Nhân viên")) {
            AllEmployeeInfor selected = employeeTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("manage-employee-detail.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                ManageEmployeeDetailController controller = fxmlLoader.getController();
                controller.SetDetail(selected);
                stage.show();
            }
        }
    }

    @FXML
    void prefixAcc(ActionEvent event) {
        if (AccSelect.getSelectionModel().getSelectedItem().equals("Khách hàng")) {
            AllUserInfor selected = customerTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("user-manage.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                UserManageController controller = fxmlLoader.getController();
                controller.SetUpInfor(selected);
                stage.show();
            }
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Tài xế")) {
            AllDriverInfor selected = driverTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("driver-manage.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                DriverManageController controller = fxmlLoader.getController();
                controller.SetInfor(selected);
                stage.show();
            }
        } else if(AccSelect.getSelectionModel().getSelectedItem().equals("Nhân viên")){
            AllEmployeeInfor selected = employeeTableView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("employee-manage.fxml"));
                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Stage stage = new Stage();
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setScene(scene);
                EmployeeManageController controller = fxmlLoader.getController();
                controller.SetInfor(selected);
                stage.show();
            }
        }
    }

    ObservableList<AllUserInfor> UserList = FXCollections.observableArrayList();

    void SetCustomerTable() {
        ArrayList<AllUserInfor> result = DAOGetAllUserInfor.getInstance().GetInfor();
        for (AllUserInfor rs : result) {
            UserList.add(new AllUserInfor(rs.getCustomer_id(), rs.getUser(), rs.getPasword(), rs.getFullname(), rs.getGender()
                    , rs.getEmail(), rs.getPhone(), rs.getDate(), rs.getCount(), rs.getSum(), rs.getPrice()));
        }
        customerIDTxt.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        customerNametxt.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        customerPhoneTxt.setCellValueFactory(new PropertyValueFactory<>("phone"));
        countRequestTxt.setCellValueFactory(new PropertyValueFactory<>("count"));
        customerTableView.setItems(UserList);

        FilteredList<AllUserInfor> filterData = new FilteredList<>(UserList, b -> true);
        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(AllUserInfor -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) return true;

                String searchKey = newValue.toLowerCase();
                if (String.valueOf(AllUserInfor.getCustomer_id()).indexOf(searchKey) > -1) {
                    return true;
                } else if (AllUserInfor.getFullname().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllUserInfor.getPhone().indexOf(searchKey) > -1) {
                    return true;
                } else if (String.valueOf(AllUserInfor.getCount()).indexOf(searchKey) > -1) {
                    return true;
                } else return false;
            });
        });

        SortedList<AllUserInfor> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(customerTableView.comparatorProperty());
        customerTableView.setItems(sortData);
    }

    ObservableList<AllDriverInfor> DriverList = FXCollections.observableArrayList();

    void SetDriverTable() {
        ArrayList<AllDriverInfor> result = DAOGetAllUserInfor.getInstance().GetDriverInfor();
        for (AllDriverInfor rs : result) {
            DriverList.add(new AllDriverInfor(rs.getDriver_id(), rs.getUser(), rs.getPassword(), rs.getFullname(), rs.getGender()
                    , rs.getEmail(), rs.getPhone(), rs.getDate(), rs.getCccd(), rs.getAddress(), rs.getStatus()
                    , rs.getId_car(), rs.getLicense(), rs.getBrand(), rs.getCount(), rs.getDistance()));
        }
        driverIDTxt.setCellValueFactory(new PropertyValueFactory<>("driver_id"));
        driverNameTxt.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        DoBTxt.setCellValueFactory(new PropertyValueFactory<>("date"));
        driverPhoneTxt.setCellValueFactory(new PropertyValueFactory<>("phone"));
        driverCarIDTxt.setCellValueFactory(new PropertyValueFactory<>("id_car"));
        countDriverRequestTxt.setCellValueFactory(new PropertyValueFactory<>("count"));
        driverTableView.setItems(DriverList);
        FilteredList<AllDriverInfor> filterData = new FilteredList<>(DriverList, b -> true);
        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(AllDriverInfor -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) return true;

                String searchKey = newValue.toLowerCase();
                if (AllDriverInfor.getStatus().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (String.valueOf(AllDriverInfor.getDriver_id()).indexOf(searchKey) > -1) {
                    return true;
                } else if (AllDriverInfor.getDate().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllDriverInfor.getPhone().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllDriverInfor.getId_car().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (String.valueOf(AllDriverInfor.getCount()).indexOf(searchKey) > -1) {
                    return true;
                } else return false;
            });
        });

        SortedList<AllDriverInfor> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(driverTableView.comparatorProperty());
        driverTableView.setItems(sortData);
    }

    ObservableList<AllEmployeeInfor> EmployeeList = FXCollections.observableArrayList();

    void SetEmployeeTable() {
        ArrayList<AllEmployeeInfor> result = DAOGetAllUserInfor.getInstance().GetEmployeeInfor();
        for (AllEmployeeInfor rs : result) {
            EmployeeList.add(new AllEmployeeInfor(rs.getEmployee_id(), rs.getFullname(), rs.getGender(), rs.getEmail(),
                    rs.getPhone(), rs.getDate(), rs.getBranch(), rs.getAddress(), rs.getCccd()));
        }
        employeeIDTxt.setCellValueFactory(new PropertyValueFactory<>("employee_id"));
        employeeNametxt.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        employeeDoBTxt.setCellValueFactory(new PropertyValueFactory<>("date"));
        employeeOfficeTxt.setCellValueFactory(new PropertyValueFactory<>("branch"));
        employeeTableView.setItems(EmployeeList);
        FilteredList<AllEmployeeInfor> filterData = new FilteredList<>(EmployeeList, b -> true);
        searchView.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData.setPredicate(AllEmployeeInfor -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) return true;

                String searchKey = newValue.toLowerCase();
                if (AllEmployeeInfor.getEmployee_id().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllEmployeeInfor.getFullname().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllEmployeeInfor.getDate().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else if (AllEmployeeInfor.getBranch().toLowerCase().indexOf(searchKey) > -1) {
                    return true;
                } else return false;
            });
        });

        SortedList<AllEmployeeInfor> sortData = new SortedList<>(filterData);
        sortData.comparatorProperty().bind(employeeTableView.comparatorProperty());
        employeeTableView.setItems(sortData);
    }

    @FXML
    void Select(ActionEvent event) {
        if (AccSelect.getSelectionModel().getSelectedItem().equals("Khách hàng")) {
            userAccView.setVisible(true);
            driverAccView.setVisible(false);
            managerAccView.setVisible(false);
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Tài xế")) {
            userAccView.setVisible(false);
            driverAccView.setVisible(true);
            managerAccView.setVisible(false);
        } else if (AccSelect.getSelectionModel().getSelectedItem().equals("Nhân viên")) {
            userAccView.setVisible(false);
            driverAccView.setVisible(false);
            managerAccView.setVisible(true);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AccSelect.setItems(FXCollections.observableArrayList("Khách hàng", "Tài xế", "Nhân viên"));
        AccSelect.getSelectionModel().select(0);
        userAccView.setVisible(true);
        driverAccView.setVisible(false);
        managerAccView.setVisible(false);
        SetCustomerTable();
        SetDriverTable();
        SetEmployeeTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            UserList.clear();
            SetCustomerTable();
            DriverList.clear();
            SetDriverTable();
            EmployeeList.clear();
            SetEmployeeTable();
        });
    }
}
