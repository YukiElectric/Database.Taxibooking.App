package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoGetDriverInfro.GetDriverInforDAO;
import com.example.demo1.daoGetUserHisoty.GetDriverHistoryDAO;
import com.example.demo1.daoResetPass.UserResetDAO;
import com.example.demo1.model.DriverInfor;
import com.example.demo1.model.EarningDriverHistory;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DriverAccountController implements Initializable {

    @FXML
    private Label countDistance;

    @FXML
    private Label countRequest;

    @FXML
    private ToggleButton edit;
    @FXML
    private ToggleButton editPass;
    @FXML
    private VBox editView;

    @FXML
    private ImageView imgView;

    @FXML
    private VBox infoView;

    @FXML
    private Label name;

    @FXML
    private JFXTextField newAddress;

    @FXML
    private JFXTextField newCCCD;

    @FXML
    private JFXTextField newDay;

    @FXML
    private JFXTextField newEmail;

    @FXML
    private JFXTextField newMonth;

    @FXML
    private JFXTextField newName;

    @FXML
    private JFXPasswordField newPass;

    @FXML
    private JFXTextField newPhone;

    @FXML
    private JFXTextField newYear;

    @FXML
    private Label rank;

    @FXML
    private JFXPasswordField reNewPass;

    @FXML
    private HBox repassView;

    @FXML
    private TableView<EarningDriverHistory> tableView;
    @FXML
    private TableColumn<EarningDriverHistory, Integer> monthTxt;
    @FXML
    private TableColumn<EarningDriverHistory, Integer> yearTxt;
    @FXML
    private TableColumn<EarningDriverHistory, Integer> counMonthRequest;
    @FXML
    private TableColumn<EarningDriverHistory, String> earningTxt;
    @FXML
    private TableColumn<EarningDriverHistory, String> bonusTxt;


    @FXML
    void cancel(ActionEvent event) {
        edit.setSelected(false);
    }

    @FXML
    void confirm(ActionEvent event) {
        if(!editPass.isSelected()) edit.setSelected(false);
        else try{
            int check = 0;
            String pass = new String(newPass.getText());
            String rePass = new String(reNewPass.getText());
            if(pass.length()<8){
                newPass.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                newPass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!rePass.equals(pass)){
                reNewPass.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                reNewPass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(check==2 && UserResetDAO.getInstance().ResetPass(TransferID.getID(),pass)){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Thay đổi thành công!");
                alert.getDialogPane().setPrefSize(220, 100);
                alert.showAndWait();
                edit.setSelected(false);
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Mật khẩu mới phải khác mật khẩu cũ và có ít nhât 8 ký tự!");
                alert.getDialogPane().setPrefSize(220, 120);
                alert.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void editPassword(ActionEvent event) {
        if(editPass.isSelected()){
            newPass.clear();
        }else newPass.setText("********");
    }
    void SetUpInfor(){
        countRequest.setText(String.valueOf(GetDriverHistoryDAO.getInstance().countRequest(TransferID.getID())));
        double distance = GetDriverHistoryDAO.getInstance().sumDistance(TransferID.getID());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(distance);
        countDistance.setText(formattedValue);
        if(distance<50){
            rank.setText("Đồng");
        }else if(distance>=50 && distance<100){
            rank.setText("Bạc");
        }else rank.setText("Vàng");
        DriverInfor rs = GetDriverInforDAO.getInstance().GetDriver(TransferID.getID());
        if(rs!=null){
            name.setText(rs.getFullname());
            newName.setText(rs.getFullname());
            newEmail.setText(rs.getEmail());
            newPhone.setText(rs.getPhone());
            newDay.setText(rs.getDate().getDate()+"");
            newMonth.setText(rs.getDate().getMonth()+1+"");
            newYear.setText(rs.getDate().getYear()+1900+"");
            newCCCD.setText(rs.getCccd());
            newAddress.setText(rs.getAddress());
        }
    }

    ObservableList<EarningDriverHistory> list = FXCollections.observableArrayList();
    void SetTable(){
        ArrayList<EarningDriverHistory> result = GetDriverHistoryDAO.getInstance().GetEaringHistory(TransferID.getID());
        for(EarningDriverHistory eDH : result){
            list.add(new EarningDriverHistory(eDH.getMonth(),eDH.getYear(),eDH.getEarnMoney(), eDH.getCountRequest()));
        }
        monthTxt.setCellValueFactory(new PropertyValueFactory<EarningDriverHistory, Integer>("month"));
        yearTxt.setCellValueFactory(new PropertyValueFactory<EarningDriverHistory, Integer>("year"));
        earningTxt.setCellValueFactory(new PropertyValueFactory<EarningDriverHistory, String>("earnMoney"));
        counMonthRequest.setCellValueFactory(new PropertyValueFactory<EarningDriverHistory, Integer>("countRequest"));
        bonusTxt.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EarningDriverHistory, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EarningDriverHistory, String> param) {
                EarningDriverHistory data = param.getValue();
                String bonusValue;
                if(data.getCountRequest()<10) bonusValue = "1.000.000";
                else if(data.getCountRequest()>=10 && data.getCountRequest()<20) bonusValue = "5.000.000";
                else bonusValue = "7.000.000";
                return new SimpleStringProperty(bonusValue);
            }
        });
        tableView.setItems(list);
    }
    @FXML
    void logOut(ActionEvent event) {
        Stage st = (Stage) newName.getScene().getWindow();
        st.close();
        FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("login.fxml"));
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editView.visibleProperty().bind(edit.selectedProperty());
        infoView.visibleProperty().bind(Bindings.not(edit.selectedProperty()));
        newPass.editableProperty().bind(editPass.selectedProperty());
        newPass.setText("********");
        repassView.visibleProperty().bind(editPass.selectedProperty());
        SetUpInfor();
        SetTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() ->{
                list.clear();
                SetTable();
                SetUpInfor();
            });
        });
        Circle clip = new Circle();
        clip.setRadius(imgView.getFitWidth()/2);
        clip.setCenterX(imgView.getFitWidth()/2);
        clip.setCenterY(imgView.getFitHeight()/2);
        imgView.setClip(clip);
    }
}
