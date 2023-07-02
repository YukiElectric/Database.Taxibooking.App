package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoGetUserHisoty.GetHistoryDAO;
import com.example.demo1.daoGetUserInfo.DAOGetUserInfor;
import com.example.demo1.daoResetPass.UserResetDAO;
import com.example.demo1.daoUserInfo.UserInfoDAO;
import com.example.demo1.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.regex.Pattern;


public class AccountController implements Initializable {

    @FXML
    private JFXButton btn_log_out;

    @FXML
    private ToggleButton edit;

    @FXML
    private VBox editView;

    @FXML
    private ToggleButton editpass;

    @FXML
    private VBox infoView;

    @FXML
    private Label name;

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
    private JFXPasswordField reNewPass;

    @FXML
    private HBox repassView;

    @FXML
    private ImageView imgView;

    @FXML
    private Label rank;
    @FXML
    private Label countDistance;

    @FXML
    private Label countRequest;
    @FXML
    private TableView<UserHistory> tableView;

    @FXML
    private TableColumn<UserHistory, String> arrivalTxt;

    @FXML
    private TableColumn<UserHistory, String> departureTxt;

    @FXML
    private TableColumn<UserHistory, String> moneyTxt;

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

    ObservableList<UserHistory> list = FXCollections.observableArrayList();
    void SetInfor(){
        countRequest.setText(String.valueOf(GetHistoryDAO.getInstance().countRequest(TransferID.getID())));
        double distance = GetHistoryDAO.getInstance().sumDistance(TransferID.getID());
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        String formattedValue = decimalFormat.format(distance);
        countDistance.setText(formattedValue);
        if (distance < 50) {
            rank.setText("Đồng");
        } else if (distance >= 50 && distance < 100) {
            rank.setText("Bạc");
        } else rank.setText("Vàng");
        UserInfo rs = DAOGetUserInfor.getInstance().GetUserInfor(TransferID.getID());
        name.setText(rs.getName());
        newName.setText(rs.getName());
        newEmail.setText(rs.getEmail());
        newPhone.setText(rs.getPhone());
        java.util.Date date = new java.util.Date(rs.getDate().getTime());
        newDay.setText(date.getDate() + "");
        newMonth.setText(date.getMonth() + 1 + "");
        newYear.setText(date.getYear() + 1900 + "");
    }
    void SetTable(){
        ArrayList<UserHistory> historyList = GetHistoryDAO.getInstance().getHistory(TransferID.getID());
        for (UserHistory usH : historyList) {
            if (usH.getStatus().equals("Hoàn thành")) {
                list.add(new UserHistory(usH.getRequest_id(), usH.getTime(), usH.getDate(), usH.getDeparture(), usH.getArrival(),
                        usH.getMoney(), usH.getDistance(), usH.getStatus(), usH.getDriver_name(), usH.getBrand(), usH.getSeatnumber(),usH.getLicense()));
            }
        }
        departureTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("departure"));
        arrivalTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("arrival"));
        moneyTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("money"));
        tableView.setItems(list);
    }

    @FXML
    void cancel(ActionEvent event) {
        edit.setSelected(false);
    }

    @FXML
    void confirm(ActionEvent event) {
        int check = 0;
        try {
            UserInfo rs = DAOGetUserInfor.getInstance().GetUserInfor(TransferID.getID());
            java.util.Date date = new java.util.Date(rs.getDate().getTime());
            String oldDate = new String(date.getDate() + "/" + (date.getMonth() + 1) + "/" + (date.getYear() + 1900));
            String fullname = new String(newName.getText());
            String email = new String(newEmail.getText());
            String phone = new String(newPhone.getText());
            String newDate = new String(newDay.getText() + "/" + newMonth.getText() + "/" + newYear.getText());
            if (fullname.equals(rs.getName()) && email.equals(rs.getEmail()) && phone.equals(rs.getPhone()) && newDate.equals(oldDate) && !editpass.isSelected()) {
                edit.setSelected(false);
            } else {
                boolean checkDbPhone = UserInfoDAO.getInstance().CheckPhone(phone);
                boolean checkDbEmail = UserInfoDAO.getInstance().CheckEmail(email);
                if (!CheckName(fullname)) {
                    newName.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    newName.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (phone.equals(rs.getPhone())) {
                    newPhone.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                } else if (!(CheckPhone(phone) && checkDbPhone)) {
                    newPhone.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    newPhone.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (email.equals(rs.getEmail())) {
                    newEmail.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                } else if (!(CheckMail(email) && checkDbEmail)) {
                    newEmail.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    newEmail.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                if (!CheckDate(newDate)) {
                    newDay.setUnFocusColor(Color.valueOf("#F04171"));
                    newMonth.setUnFocusColor(Color.valueOf("#F04171"));
                    newYear.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    newDay.setUnFocusColor(Color.valueOf("#0097A7"));
                    newMonth.setUnFocusColor(Color.valueOf("#0097A7"));
                    newYear.setUnFocusColor(Color.valueOf("#0097A7"));
                    check++;
                }
                Date dob = new SimpleDateFormat(DATE_FORMAT).parse(newDate);
                long DoB = dob.getTime();
                java.sql.Date userDoB = new java.sql.Date(DoB);
                if (check == 4 && !editpass.isSelected()) {
                    if (DAOGetUserInfor.getInstance().UpdateUserInfor(TransferID.getID(), fullname, email, phone, userDoB)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("");
                        alert.setHeaderText("");
                        alert.setContentText("Thay đổi thành công!");
                        alert.getDialogPane().setPrefSize(220, 100);
                        alert.showAndWait();
                        edit.setSelected(false);
                        SetInfor();
                    } else {
                        Alert info = new Alert(Alert.AlertType.ERROR);
                        info.setTitle("");
                        info.setHeaderText("");
                        info.setContentText("Lỗi hệ thống không thể thay đổi!");
                        info.getDialogPane().setPrefSize(220, 100);
                        info.showAndWait();
                        edit.setSelected(false);
                    }
                }
                if (check == 4 && editpass.isSelected()) {
                    int status = 0;
                    String pass = new String(newPass.getText());
                    String rePass = new String(reNewPass.getText());
                    if (pass.length() < 8) {
                        newPass.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        newPass.setUnFocusColor(Color.valueOf("#0097A7"));
                        status++;
                    }
                    if (!rePass.equals(pass)) {
                        reNewPass.setUnFocusColor(Color.valueOf("#F04171"));
                    } else {
                        reNewPass.setUnFocusColor(Color.valueOf("#0097A7"));
                        status++;
                    }
                    if (status == 2) {
                        if (UserResetDAO.getInstance().ResetPass(TransferID.getID(), pass)) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("");
                            alert.setHeaderText("");
                            alert.setContentText("Thay đổi thành công!");
                            alert.getDialogPane().setPrefSize(220, 100);
                            alert.showAndWait();
                            edit.setSelected(false);
                            SetInfor();
                        } else {
                            Alert info = new Alert(Alert.AlertType.ERROR);
                            info.setTitle("");
                            info.setHeaderText("");
                            info.setContentText("Lỗi hệ thống không thể thay đổi!");
                            info.getDialogPane().setPrefSize(220, 100);
                            info.showAndWait();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Alert info = new Alert(Alert.AlertType.ERROR);
            info.setTitle("");
            info.setHeaderText("");
            info.setContentText("Không đủ thông tin để thay đổi!");
            info.getDialogPane().setPrefSize(220, 100);
            info.showAndWait();
        }
    }

    @FXML
    void logOut(ActionEvent event) {
        TransferData.statusProperty().set(!TransferData.isStatus());
        Stage st = (Stage) btn_log_out.getScene().getWindow();
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

    @FXML
    void setPass(ActionEvent event) {
        if (editpass.isSelected()) {
            newPass.clear();
        } else newPass.setText("********");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        editView.visibleProperty().bind(edit.selectedProperty());
        infoView.visibleProperty().bind(Bindings.not(edit.selectedProperty()));
        repassView.visibleProperty().bind(editpass.selectedProperty());
        newPass.editableProperty().bind(editpass.selectedProperty());
        newPass.setText("********");
        SetInfor();
        SetTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                list.clear();
                SetTable();
                SetInfor();
            });
        });
        Circle clip = new Circle();
        clip.setRadius(imgView.getFitWidth() / 2);
        clip.setCenterX(imgView.getFitWidth() / 2);
        clip.setCenterY(imgView.getFitHeight() / 2);
        imgView.setClip(clip);
    }
}
