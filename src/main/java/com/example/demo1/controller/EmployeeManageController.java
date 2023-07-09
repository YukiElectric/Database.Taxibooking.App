package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoSetEmployeeInfor.SetEmployeeInforDAO;
import com.example.demo1.daoSetLog.SetLogDAO;
import com.example.demo1.model.AllEmployeeInfor;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceDetails;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeManageController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private JFXTextField address;

    @FXML
    private JFXTextField branch;

    @FXML
    private JFXTextField cccd;

    @FXML
    private JFXTextField day;

    @FXML
    private JFXTextField email;

    @FXML
    private ToggleGroup gender;

    @FXML
    private JFXTextField month;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXTextField year;

    @FXML
    private ToggleButton female;

    @FXML
    private ToggleButton male;

    boolean status = true;
    AllEmployeeInfor inputInfor = null;

    void close(){
        Stage stage = (Stage) day.getScene().getWindow();
        stage.close();
    }
    private static final String API_KEY = "AIzaSyDY9JFHpiLuYUTI4t1phuKnIpZYvqygpfg";
    boolean CheckLocation(String place) {
        if (place.equals("")) return false;
        String location = "Việt Nam";
        try {
            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
            PlacesSearchResponse searchResponse = PlacesApi.textSearchQuery(context, place + " " + location).await();
            if (searchResponse.results.length > 0) {
                PlacesSearchResult firstResult = searchResponse.results[0];
                PlaceDetails placeDetails = PlacesApi.placeDetails(context, firstResult.placeId).await();
                if (placeDetails.geometry != null && placeDetails.geometry.location != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    static boolean CheckName(String name) {
        if (name.matches("^[a-zA-Z\\\\s]*$") || name.matches("^[\\p{L}\\s']+\\s[\\p{L}\\s']+$")) {
            String[] s = name.split("\\s");
            for (String string : s) {
                if (!Character.isUpperCase(string.charAt(0)))
                    return false;
                for (int i = 1; i < string.length(); i++) {
                    if (!Character.isLowerCase(string.charAt(i)))
                        return false;
                }
            }
        } else
            return false;
        return true;
    }

    static boolean CheckMail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
                + "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    static boolean CheckPhone(String phone) {
        String[] Viettel = {"032", "033", "034", "035", "036", "037", "038", "039"};
        String[] Mobiphone = {"070", "076", "077", "078", "079"};
        String[] Vinaphone = {"081", "082", "083", "084", "085"};
        String[] VietNamMobile = {"056", "058", "059"};
        if (phone.matches("^\\d{10}$")) {
            for (String phoneNumber : Viettel)
                if (phone.startsWith(phoneNumber))
                    break;
            for (String phoneNumber : Mobiphone)
                if (!phone.startsWith(phoneNumber))
                    break;
            for (String phoneNumber : Vinaphone)
                if (!phone.startsWith(phoneNumber))
                    break;
            for (String phoneNumber : VietNamMobile)
                if (!phone.startsWith(phoneNumber))
                    break;
        } else
            return false;
        return true;
    }
    final static String DATE_FORMAT = "dd/MM/yyyy";

    static boolean CheckDate(String date) {
        if (date.equals("")) return false;
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            Date dob = df.parse(date);
            if (dob.before(new Date())) return true;
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    static boolean CheckCCCD(String input) {
        return input.matches("\\d{12}");
    }

    static boolean CheckBranch(String input){
        String regex = "^[\\p{L} ]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    void SetInfor(AllEmployeeInfor result){
        name.setText(result.getFullname());
        if(result.getGender().equals("Nam")) male.setSelected(true);
        else female.setSelected(true);
        String[] date = result.getDate().split("/");
        day.setText(date[0]);
        month.setText(date[1]);
        year.setText(date[2]);
        email.setText(result.getEmail());
        phone.setText(result.getPhone());
        cccd.setText(result.getCccd());
        branch.setText(result.getBranch());
        address.setText(result.getAddress());
        status = false;
        inputInfor = result;
    }

    @FXML
    void deleteAct(ActionEvent event) {
        name.clear();
        male.setSelected(true);
        day.clear();
        month.clear();
        year.clear();
        email.clear();
        phone.clear();
        cccd.clear();
        branch.clear();
        address.clear();
    }

    @FXML
    void saveAct(ActionEvent event) throws ParseException {
        if(!status){
            try {
                String newName = name.getText();
                String newGender;
                if(male.isSelected()) newGender = "Nam";
                else newGender = "Nữ";
                String newPhone = phone.getText();
                String newEmail = email.getText();
                String newDate = new String(day.getText()+"/"+month.getText()+"/"+year.getText());
                String newCccd = cccd.getText();
                String newAddress = address.getText();
                String newBranch = branch.getText();
                Date dob = new SimpleDateFormat(DATE_FORMAT).parse(newDate);
                long DoB = dob.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                boolean checkPhone = SetEmployeeInforDAO.getInstance().checkEmployeePhone(newPhone);
                boolean checkEmail = SetEmployeeInforDAO.getInstance().checkEmployeeEmail(newEmail);
                if(newName.equals(inputInfor.getFullname()) && newGender.equals(inputInfor.getGender()) && newDate.equals(inputInfor.getDate())
                        && newPhone.equals(inputInfor.getPhone()) && newEmail.equals(inputInfor.getEmail()) &&  newCccd.equals(inputInfor.getCccd())
                        && newAddress.equals(inputInfor.getAddress()) && newBranch.equals(inputInfor.getBranch())){
                    close();
                }else {
                    int check = 0;
                    if (!CheckName(newName)) {
                        name.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        name.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if (newPhone.equals(inputInfor.getPhone())) {
                        phone.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    } else if (!(CheckPhone(newPhone) && checkPhone)) {
                        phone.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        phone.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if (newEmail.equals(inputInfor.getEmail())) {
                        email.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    } else if (!(CheckMail(newEmail) && checkEmail)) {
                        email.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        email.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if (!CheckDate(newDate)) {
                        day.setUnFocusColor(Color.valueOf("#F04171"));
                        month.setUnFocusColor(Color.valueOf("#F04171"));
                        year.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        day.setUnFocusColor(Color.valueOf("#0097A7"));
                        month.setUnFocusColor(Color.valueOf("#0097A7"));
                        year.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(!CheckLocation(newAddress)){
                        address.setUnFocusColor(Color.valueOf("#F04171"));
                    }else{
                        address.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(!CheckCCCD(newCccd)){
                        cccd.setUnFocusColor(Color.valueOf("#F04171"));
                    }else{
                        cccd.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(!CheckBranch(newBranch)){
                        branch.setUnFocusColor(Color.valueOf("#F04171"));
                    }else{
                        branch.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(check==7){
                        if(SetEmployeeInforDAO.getInstance().updateEmployeeInfor(inputInfor.getEmployee_id(),newName,newGender,newPhone,newEmail
                                ,userDoB,newCccd,newAddress,newBranch)){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText("");
                            alert.setContentText("Thay đổi thành công!");
                            alert.getDialogPane().setPrefSize(220, 100);
                            alert.showAndWait();
                            close();
                            SetLogDAO.getInstance().SetRepairEmployeeAccLog(inputInfor.getEmployee_id(),TransferID.getID());
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
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Thiếu thông tin không thể thay đổi!");
                alert.getDialogPane().setPrefSize(220, 100);
                alert.showAndWait();
            }
        }else {
            try {
                String newName = name.getText();
                String newGender;
                if(male.isSelected()) newGender = "Nam";
                else newGender = "Nữ";
                String newPhone = phone.getText();
                String newEmail = email.getText();
                String newDate = new String(day.getText()+"/"+month.getText()+"/"+year.getText());
                String newCccd = cccd.getText();
                String newAddress = address.getText();
                String newBranch = branch.getText();
                Date dob = new SimpleDateFormat(DATE_FORMAT).parse(newDate);
                long DoB = dob.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                boolean checkPhone = SetEmployeeInforDAO.getInstance().checkEmployeePhone(newPhone);
                boolean checkEmail = SetEmployeeInforDAO.getInstance().checkEmployeeEmail(newEmail);
                int check = 0;
                if (!CheckName(newName)) {
                    name.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    name.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (!(CheckPhone(newPhone) && checkPhone)) {
                    phone.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    phone.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (!(CheckMail(newEmail) && checkEmail)) {
                    email.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    email.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (!CheckDate(newDate)) {
                    day.setUnFocusColor(Color.valueOf("#F04171"));
                    month.setUnFocusColor(Color.valueOf("#F04171"));
                    year.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    day.setUnFocusColor(Color.valueOf("#0097A7"));
                    month.setUnFocusColor(Color.valueOf("#0097A7"));
                    year.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if(!CheckLocation(newAddress)){
                    address.setUnFocusColor(Color.valueOf("#F04171"));
                }else{
                    address.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if(!CheckCCCD(newCccd)){
                    cccd.setUnFocusColor(Color.valueOf("#F04171"));
                }else{
                    cccd.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if(!CheckBranch(newBranch)){
                    branch.setUnFocusColor(Color.valueOf("#F04171"));
                }else{
                    branch.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if(check==7){
                    if(SetEmployeeInforDAO.getInstance().insertEmployeeInfor(TransferID.getID(),newName,newGender,newPhone,newEmail,userDoB,newCccd,newAddress,newBranch)){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Thêm tài khoản thành công!");
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
        ((Stage) name.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        App.setDraggable(titleView);
    }
}
