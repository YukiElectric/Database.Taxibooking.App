package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoUserInfo.UserInfoDAO;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SignController {

    @FXML
    private AnchorPane titleView;

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
    private JFXPasswordField pass;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXPasswordField repass;

    @FXML
    private JFXTextField user;

    @FXML
    private JFXTextField year;


    @FXML
    void closeApp(MouseEvent event) {
        System.exit(0);
    }
    void close() {
        Stage stage = (Stage) phone.getScene().getWindow();
        stage.close();
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

    static boolean CheckUser(String user) {
        if (user == null)
            return false;
        return user.matches("^[a-z0-9_-]+$");
    }

    static boolean CheckPhone(String phone) {
        String[] Viettel = { "032", "033", "034", "035", "036", "037", "038", "039" };
        String[] Mobiphone = { "070", "076", "077", "078", "079" };
        String[] Vinaphone = { "081", "082", "083", "084", "085" };
        String[] VietNamMobile = { "056", "058", "059" };
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
    static boolean CheckDate(String date){
        if(date.equals("")) return false;
        try{
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            Date dob = df.parse(date);
            if(dob.before(new Date())) return true;
        }catch (Exception e){
            return false;
        }
        return false;
    }

    void SetLogin(){
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
        close();
    }

    @FXML
    void login(MouseEvent event) {
        SetLogin();
    }

    @FXML
    void minius(MouseEvent event) {
        Stage stage = (Stage) phone.getScene().getWindow();
        stage.toBack();
    }

    @FXML
    void sign(ActionEvent event) {
        int check = 0;
        try {
            String userFullName = new String(name.getText());
            String userGender = new String(((ToggleButton) gender.getSelectedToggle()).getText());
            String userPhone = new String(phone.getText());
            String userEmail = new String(email.getText());
            String userDob = new String(day.getText()+"/"+month.getText()+"/"+year.getText());
            String userName = new String(user.getText());
            String userPass = new String(pass.getText());
            String rePass = new String(repass.getText());
            boolean checkDbName = UserInfoDAO.getInstance().CheckUser(userName);
            boolean checkDbPhone = UserInfoDAO.getInstance().CheckPhone(userPhone);
            boolean checkDbEmail = UserInfoDAO.getInstance().CheckEmail(userEmail);
            if (!CheckName(userFullName)) {
                name.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                name.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if (!(CheckPhone(userPhone) && checkDbPhone)) {
                phone.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                phone.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if (!(CheckMail(userEmail) && checkDbEmail)) {
                email.setUnFocusColor(Color.valueOf("#F04171"));
            } else {
                email.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!CheckDate(userDob)){
                day.setUnFocusColor(Color.valueOf("#F04171"));
                month.setUnFocusColor(Color.valueOf("#F04171"));
                year.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                day.setUnFocusColor(Color.valueOf("#0097A7"));
                month.setUnFocusColor(Color.valueOf("#0097A7"));
                year.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!(CheckUser(userName) && checkDbName)){
                user.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                user.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(userPass.length()<8){
                pass.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                pass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(!rePass.equals(userPass)){
                repass.setUnFocusColor(Color.valueOf("#F04171"));
            }else{
                repass.setUnFocusColor(Color.valueOf("#0097A7"));
                check++;
            }
            if(check==7 && !userGender.equals("")){
                Date date = new SimpleDateFormat(DATE_FORMAT).parse(userDob);
                long DoB = date.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                int ID = UserInfoDAO.getInstance().InsertAccount(userName,userPass);
                if(UserInfoDAO.getInstance().IsertInformation(ID,userFullName,userGender,userPhone,userEmail,userDoB)){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText("Đăng ký thành công!");
                    alert.getDialogPane().setPrefSize(220,100);
                    alert.showAndWait();
                    SetLogin();
                }else {
                    Alert info = new Alert(Alert.AlertType.ERROR);
                    info.setTitle("");
                    info.setHeaderText("");
                    info.setContentText("Lỗi hệ thống không thể đăng ký!");
                    info.getDialogPane().setPrefSize(220,100);
                    info.showAndWait();
                    SetLogin();
                }
            }
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Bạn đã điền thiếu thông tin hoặc lỗi hệ thống");
            alert.getDialogPane().setPrefSize(220,100);
            alert.showAndWait();
        }
    }

    public void initialize() {
        App.setDraggable(titleView);
    }
}
