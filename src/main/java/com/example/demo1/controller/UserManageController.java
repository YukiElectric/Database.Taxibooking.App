package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoSetLog.SetLogDAO;
import com.example.demo1.daoUserInfo.UserInfoDAO;
import com.example.demo1.model.AllUserInfor;
import com.example.demo1.model.RunningStatus;
import com.example.demo1.model.TransferID;
import com.jfoenix.controls.JFXPasswordField;
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

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class UserManageController implements Initializable {
    @FXML
    private AnchorPane titleView;

    @FXML
    private JFXTextField day;

    @FXML
    private JFXTextField email;

    @FXML
    private ToggleButton female;

    @FXML
    private ToggleGroup gender;

    @FXML
    private ToggleButton male;

    @FXML
    private JFXTextField month;

    @FXML
    private JFXTextField name;

    @FXML
    private JFXPasswordField pass;

    @FXML
    private JFXTextField phone;

    @FXML
    private JFXTextField user;

    @FXML
    private JFXTextField year;

    void close(){
        Stage stage = (Stage) year.getScene().getWindow();
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
    boolean status = true;
    AllUserInfor inputInfor = null;
    void SetUpInfor(AllUserInfor result){
        name.setText(result.getFullname());
        String[] date = result.getDate().split("/");
        day.setText(date[0]);
        month.setText(date[1]);
        year.setText(date[2]);
        if(result.getGender().equals("Nam")) male.setSelected(true);
        else female.setSelected(false);
        phone.setText(result.getPhone());
        email.setText(result.getEmail());
        user.setText(result.getUser());
        pass.setText(result.getPasword());
        status = false;
        inputInfor = result;
    }
    static boolean CheckUser(String user) {
        if (user == null)
            return false;
        return user.matches("^[a-z0-9_-]+$");
    }
    @FXML
    void deleteAct(ActionEvent event) {
        name.clear();
        month.clear();
        day.clear();
        year.clear();
        male.setSelected(true);
        email.clear();
        phone.clear();
        user.clear();
        pass.clear();
    }

    @FXML
    void saveAct(ActionEvent event) {
        if (!status) {
            try {
                String newName = name.getText();
                String newGender;
                if(male.isSelected()) newGender = "Nam";
                else newGender = "Nữ";
                String newPhone = phone.getText();
                String newEmail = email.getText();
                String newDate = new String(day.getText()+"/"+month.getText()+"/"+year.getText());
                String newUser = user.getText();
                String newPass = pass.getText();
                Date dob = new SimpleDateFormat(DATE_FORMAT).parse(newDate);
                long DoB = dob.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                boolean checkUserName = UserInfoDAO.getInstance().CheckUser(newUser);
                boolean checkPhone = UserInfoDAO.getInstance().CheckPhone(newPhone);
                boolean checkEmail = UserInfoDAO.getInstance().CheckEmail(newEmail);
                if(newName.equals(inputInfor.getFullname()) && newGender.equals(inputInfor.getGender()) && newDate.equals(inputInfor.getDate())
                        && newPhone.equals(inputInfor.getPhone()) && newEmail.equals(inputInfor.getEmail())
                        && newUser.equals(inputInfor.getUser()) && newPass.equals(inputInfor.getPasword())){
                    close();
                }else{
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
                    if(newUser.equals(inputInfor.getUser())){
                        user.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }else if(!(CheckUser(newUser) && checkUserName)){
                        user.setUnFocusColor(Color.valueOf("#F04171"));
                    }else{
                        user.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(newPass.equals(inputInfor.getPasword())){
                        pass.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }else if(newPass.length()<8){
                        pass.setUnFocusColor(Color.valueOf("#F04171"));
                    }else{
                        pass.setUnFocusColor(Color.valueOf("#0097A7"));
                        check++;
                    }
                    if(check==6){
                        boolean updateAcc = UserInfoDAO.getInstance().UpdateCustomerAcc(inputInfor.getCustomer_id(),newUser,newPass);
                        boolean updateInfor = UserInfoDAO.getInstance().UpdateUserInfor(inputInfor.getCustomer_id(),newName,newGender,newEmail,newPhone,userDoB);
                        if(updateInfor && updateAcc){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText("");
                            alert.setContentText("Thay đổi thành công!");
                            alert.getDialogPane().setPrefSize(220, 100);
                            alert.showAndWait();
                            close();
                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                            SetLogDAO.getInstance().SetRepairCustomerAccLog(inputInfor.getCustomer_id(), TransferID.getID());
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
                String newUser = user.getText();
                String newPass = pass.getText();
                Date dob = new SimpleDateFormat(DATE_FORMAT).parse(newDate);
                long DoB = dob.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                boolean checkUserName = UserInfoDAO.getInstance().CheckUser(newUser);
                boolean checkPhone = UserInfoDAO.getInstance().CheckPhone(newPhone);
                boolean checkEmail = UserInfoDAO.getInstance().CheckEmail(newEmail);
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
                if (!(CheckUser(newUser) && checkUserName)) {
                    user.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    user.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (newPass.length() < 8) {
                    pass.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    pass.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (check == 6) {
                    int ID = UserInfoDAO.getInstance().InsertAccount(newUser, newPass);
                    if (UserInfoDAO.getInstance().IsertInformation(ID, newName, newGender, newPhone, newEmail, userDoB)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Thêm tài khoản thành công!");
                        alert.getDialogPane().setPrefSize(220, 100);
                        alert.showAndWait();
                        close();
                        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                        SetLogDAO.getInstance().SetAddCustomerAccLog(ID,TransferID.getID());
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
