package com.example.demo1.controller;

import com.example.demo1.App;
import com.example.demo1.daoGetUserHisoty.GetHistoryDAO;
import com.example.demo1.daoRequest.SetRequestDAO;
import com.example.demo1.model.*;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

public class BookingController implements Initializable {

    @FXML
    private JFXButton act_booking;

    @FXML
    private JFXButton act_cancel;

    @FXML
    private JFXTextField arrival;

    @FXML
    private VBox bookingView;

    @FXML
    private VBox booking_scr;

    @FXML
    private ToggleButton btn_booking;

    @FXML
    private ToggleButton btn_food;

    @FXML
    private ToggleButton btn_history;

    @FXML
    private ToggleButton btn_ship;

    @FXML
    private JFXTextField day;

    @FXML
    private JFXTextField departure;

    @FXML
    private VBox foodView;

    @FXML
    private ToggleGroup gr2;

    @FXML
    private ToggleGroup gr3;

    @FXML
    private VBox historyView;

    @FXML
    private JFXTextField hour;

    @FXML
    private JFXTextField minute;

    @FXML
    private Label money;

    @FXML
    private JFXTextField month;

    @FXML
    private VBox shipView;

    @FXML
    private HBox time_select;

    @FXML
    private JFXToggleButton time_tg;

    @FXML
    private JFXTextField voucher;

    @FXML
    private JFXTextField year;

    @FXML
    private JFXRadioButton car16;

    @FXML
    private JFXRadioButton car4;

    @FXML
    private JFXRadioButton car7;

    @FXML
    private JFXRadioButton carV4;

    @FXML
    private TableView<UserHistory> tableView;

    @FXML
    private TableColumn<UserHistory, String> arrivalTxt;

    @FXML
    private TableColumn<UserHistory, String> departureTxt;

    @FXML
    private TableColumn<UserHistory, String> moneyTxt;

    @FXML
    private TableColumn<UserHistory, String> statusTxt;

    void setCar(UserHistory usH) {
        if (usH.getBrand().startsWith("Vinfast") && usH.getSeatnumber() == 4) {
            carV4.setSelected(true);
        } else {
            switch (usH.getSeatnumber()) {
                case 4:
                    car4.setSelected(true);
                    break;
                case 7:
                    car7.setSelected(true);
                    break;
                default:
                    car16.setSelected(true);
                    break;
            }
        }
    }

    private static final String API_KEY = "AIzaSyBsXqkH0yxkIrghjSqoQScksQWaYOgJSkI";

    void SetUp() {
        UserHistory result = GetHistoryDAO.getInstance().checkStatus(TransferID.getID());
        if (result != null) {
            act_booking.setVisible(false);
            act_cancel.setVisible(true);
            booking_scr.setDisable(true);
            TransferVoucher.setBookingStatus(false);
            departure.setText(result.getDeparture());
            arrival.setText(result.getArrival());
            setCar(result);
            if (result.getDate().after(new Date())) {
                time_tg.setSelected(true);
                hour.setText(result.getTime().getHours() + "");
                minute.setText(result.getTime().getMinutes() + "");
                day.setText(result.getDate().getDate() + "");
                month.setText(result.getDate().getMonth() + 1 + "");
                year.setText(result.getDate().getYear() + 1900 + "");
            }
            money.setText(result.getMoney());
            if (result.getStatus().equals("Đang tiến hành")) act_cancel.setDisable(true);
        }
    }

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

    double calculateDistance(String originAddress, String destinationAddress) {
        if (originAddress.equals("") || destinationAddress.equals("")) return 0;
        try {
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(API_KEY)
                    .build();

            GeocodingResult[] originResults = com.google.maps.GeocodingApi.geocode(context, originAddress).await();
            GeocodingResult[] destinationResults = com.google.maps.GeocodingApi.geocode(context, destinationAddress).await();

            if (originResults.length > 0 && destinationResults.length > 0) {
                LatLng originLatLng = originResults[0].geometry.location;
                LatLng destinationLatLng = destinationResults[0].geometry.location;

                DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                        .origins(originLatLng)
                        .destinations(destinationLatLng)
                        .mode(TravelMode.DRIVING)
                        .await();

                return distanceMatrix.rows[0].elements[0].distance.inMeters / 1000.0;
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static String formatCurrency(double value) {
        Locale vietnameseLocale = new Locale("vi", "VN");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(vietnameseLocale);
        symbols.setCurrencySymbol("");
        symbols.setGroupingSeparator('.');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
        decimalFormat.setGroupingUsed(true);
        decimalFormat.setGroupingSize(3);
        decimalFormat.setMinimumFractionDigits(0);
        return decimalFormat.format(value).trim();
    }

    @FXML
    void voucherSet(ActionEvent event) {
        TransferVoucher.statusProperty().set(!TransferVoucher.getStatus());
    }

    boolean CheckInput() {
        int check = 0;
        String departureView = new String(departure.getText());
        String arrivalView = new String(arrival.getText());
        if (departureView.equals("") && arrivalView.equals("")) return false;
        if (CheckLocation(departureView)) {
            check++;
            departure.setUnFocusColor(Color.valueOf("#0097A7"));
        } else {
            departure.setUnFocusColor(Color.valueOf("#F04171"));
        }
        if (CheckLocation(arrivalView)) {
            check++;
            arrival.setUnFocusColor(Color.valueOf("#0097A7"));
        } else {
            arrival.setUnFocusColor(Color.valueOf("#F04171"));
        }
        if (check == 2) {
            double carValueChoice;
            if (car4.isSelected()) carValueChoice = CarType.Car4.getMoney();
            else if (carV4.isSelected()) carValueChoice = CarType.CarV4.getMoney();
            else if (car7.isSelected()) carValueChoice = CarType.Car7.getMoney();
            else carValueChoice = CarType.Car16.getMoney();
            double priceValue = Math.ceil(calculateDistance(departure.getText(), arrival.getText()) * carValueChoice / 1000.0) * 1000.0;
            TransferMoney.setStrPr(formatCurrency(priceValue));
            return true;
        }
        return false;
    }

    final static String DATE_FORMAT = "dd/MM/yyyy";

    final static String TIME_FORMAT = "HH:mm";

    static Date CheckDate(String date) {
        if (date.equals("")) return null;
        try {
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            Date dob = df.parse(date);
            if (dob.after(new Date())) return dob;
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    void SetCantCreatRequestInfor() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Lỗi hệ thống không thể tạo đơn!");
        alert.getDialogPane().setPrefSize(220, 100);
        alert.showAndWait();
        ResetInfor();
    }

    void CantCancelRequest() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Tài xế đã đến nơi");
        alert.getDialogPane().setPrefSize(220, 100);
        alert.showAndWait();
        check = true;
        act_cancel.setDisable(true);
        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
    }

    void CompleteRequest() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("Đơn đã được hoàn thành");
        alert.getDialogPane().setPrefSize(220, 100);
        alert.showAndWait();
        check = true;
        ResetInfor();
        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
    }

    static Time CheckTime(String time) {
        if (time.equals("")) return null;

        String[] split = time.split(":");
        if (split.length != 2) return null;
        try {
            int hourValue = Integer.parseInt(split[0]);
            int minuteValue = Integer.parseInt(split[1]);

            boolean isValidHour = hourValue >= 0 && hourValue < 24;
            boolean isValidMinute = minuteValue >= 0 && minuteValue < 60;
            if (!(isValidMinute && isValidHour)) return null;
            DateFormat df = new SimpleDateFormat(TIME_FORMAT);
            Date date = df.parse(time);
            Time returnTime = new Time(date.getTime());
            return returnTime;
        } catch (Exception e) {
            return null;
        }
    }
    boolean check = true;
    Task<Void> task1;
    Task<Void> task2;

    @FXML
    void booking(ActionEvent event) {
        String hourView = new String(hour.getText());
        String minuteView = new String(minute.getText());
        String dayView = new String(day.getText());
        String monthView = new String(month.getText());
        String yearView = new String(year.getText());
        try {
            String departureView = new String(departure.getText());
            String arrivalView = new String(arrival.getText());
            double carValueChoice = 0;
            int choice;
            if (car4.isSelected()) {
                carValueChoice = CarType.Car4.getMoney();
                choice = 0;
            } else if (carV4.isSelected()) {
                carValueChoice = CarType.CarV4.getMoney();
                choice = 1;
            } else if (car7.isSelected()) {
                carValueChoice = CarType.Car7.getMoney();
                choice = 2;
            } else {
                carValueChoice = CarType.Car16.getMoney();
                choice = 3;
            }
            double distanceValue = calculateDistance(departureView, arrivalView);
            double priceValue = Math.ceil(distanceValue * carValueChoice / 1000.0) * 1000.0;
            if (time_tg.isSelected()) {
                String time = new String(hourView + ":" + minuteView);
                String date = new String(dayView + "/" + monthView + "/" + yearView);
                Time timeRequest = CheckTime(time);
                Date dateRequest = CheckDate(date);
                if (timeRequest == null) {
                    hour.setUnFocusColor(Color.valueOf("#F04171"));
                    minute.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    hour.setUnFocusColor(Color.valueOf("#0097A7"));
                    minute.setUnFocusColor(Color.valueOf("#0097A7"));
                }
                if (dateRequest == null) {
                    day.setUnFocusColor(Color.valueOf("#F04171"));
                    month.setUnFocusColor(Color.valueOf("#F04171"));
                    year.setUnFocusColor(Color.valueOf("#F04171"));
                } else {
                    day.setUnFocusColor(Color.valueOf("#0097A7"));
                    month.setUnFocusColor(Color.valueOf("#0097A7"));
                    year.setUnFocusColor(Color.valueOf("#0097A7"));
                }
                if (timeRequest != null && dateRequest != null && CheckInput()) {
                    TransferMoney.setBlPr(!TransferMoney.isBlPr());
                    act_booking.setVisible(false);
                    act_cancel.setVisible(true);
                    booking_scr.setDisable(true);
                    TransferVoucher.setBookingStatus(false);
                    long dateTemp = dateRequest.getTime();
                    java.sql.Date DateRequest = new java.sql.Date(dateTemp);
                    new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    if (!SetRequestDAO.getInstance().sendRequest(TransferID.getID(), departureView, arrivalView, timeRequest
                                            , DateRequest, priceValue, distanceValue, choice)) {
                                        Platform.runLater(() -> {
                                            SetCantCreatRequestInfor();
                                        });
                                    } else {
                                        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                        // if (task != null) task.cancel();
                                        if(check){
                                            new Service<Void>() {       //task1
                                                @Override
                                                protected Task<Void> createTask() {
                                                    return  new Task<Void>() {
                                                        @Override
                                                        protected Void call() throws Exception {
                                                            if(!check) return null;
                                                            while (true) {
                                                                if (SetRequestDAO.getInstance().CheckCustomerRequest(TransferID.getID()))
                                                                    break;
                                                            }
                                                            Platform.runLater(() -> {
                                                                CantCancelRequest();
                                                            });
                                                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                                            return null;
                                                        }
                                                    };
                                                }
                                            }.start();
                                            new Service<Void>() {  //task2
                                                @Override
                                                protected Task<Void> createTask() {
                                                    return new Task<Void>() {
                                                        @Override
                                                        protected Void call() throws Exception {
                                                            if(!check) return null;
                                                            while (true) {
                                                                if (SetRequestDAO.getInstance().CheckCompleteRequest(TransferID.getID())) break;
                                                            }
                                                            Platform.runLater(() -> {
                                                                CompleteRequest();
                                                                act_cancel.setDisable(false);
                                                            });
                                                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                                            return null;
                                                        }
                                                    };
                                                }
                                            }.start();
                                        }
                                    }
                                    return null;
                                }
                            };
                        }
                    }.start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText("Không đủ thông tin để tạo đơn!");
                    alert.getDialogPane().setPrefSize(220, 100);
                    alert.showAndWait();
                }
            } else {
                if (CheckInput()) {
                    LocalTime currentTime = LocalTime.now();
                    Time timeRequest = Time.valueOf(currentTime);
                    java.sql.Date dateRequest = java.sql.Date.valueOf(LocalDate.now());
                    TransferMoney.setBlPr(!TransferMoney.isBlPr());
                    act_booking.setVisible(false);
                    act_cancel.setVisible(true);
                    booking_scr.setDisable(true);
                    TransferVoucher.setBookingStatus(false);
                    new Service<Void>() {
                        @Override
                        protected Task<Void> createTask() {
                            return new Task<Void>() {
                                @Override
                                protected Void call() throws Exception {
                                    if (!SetRequestDAO.getInstance().sendRequest(TransferID.getID(), departureView, arrivalView, timeRequest
                                            , dateRequest, priceValue, distanceValue, choice)) {
                                        Platform.runLater(() -> {
                                            SetCantCreatRequestInfor();
                                        });
                                    } else {
                                        RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                        // if (task != null) task.cancel();
                                        if(check){
                                            new Service<Void>() { //task1
                                                @Override
                                                protected Task<Void> createTask() {

                                                    return new Task<Void>() {
                                                        @Override
                                                        protected Void call() throws Exception {
                                                            while (true) {
                                                                if (SetRequestDAO.getInstance().CheckCustomerRequest(TransferID.getID()))
                                                                    break;
                                                            }
                                                            Platform.runLater(() -> {
                                                                CantCancelRequest();
                                                            });
                                                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                                            return null;
                                                        }
                                                    };
                                                }
                                            }.start();
                                            new Service<Void>() {  //task2
                                                @Override
                                                protected Task<Void> createTask() {
                                                    return new Task<Void>() {
                                                        @Override
                                                        protected Void call() throws Exception {
                                                            while (true) {
                                                                if (SetRequestDAO.getInstance().CheckCompleteRequest(TransferID.getID())) break;
                                                            }
                                                            Platform.runLater(() -> {
                                                                CompleteRequest();
                                                                act_cancel.setDisable(false);
                                                            });
                                                            RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
                                                            return null;
                                                        }
                                                    };
                                                }
                                            }.start();
                                        }
                                    }
                                    return null;
                                }
                            };
                        }
                    }.start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("");
                    alert.setHeaderText("");
                    alert.setContentText("Không đủ thông tin để tạo đơn!");
                    alert.getDialogPane().setPrefSize(220, 100);
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void ResetInfor() {
        act_booking.setVisible(true);
        act_cancel.setVisible(false);
        booking_scr.setDisable(false);
        TransferVoucher.setBookingStatus(true);
        departure.clear();
        arrival.clear();
        time_tg.setSelected(false);
        car4.setSelected(true);
        day.setText("");
        month.setText("");
        year.setText("");
        hour.setText("");
        minute.setText("");
        TransferData.setVoucher(null);
        TransferMoney.setStrPr("0");
        TransferMoney.setBlPr(!TransferMoney.isBlPr());
    }

    @FXML
    void cancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("");
        alert.setHeaderText("Bạn có chắc chắn muốn hủy đơn không?");
        alert.setContentText("");
        alert.getDialogPane().setPrefSize(220, 100);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK) {
            if (!SetRequestDAO.getInstance().updateRequest(TransferID.getID())) {
                Alert alert1 = new Alert(Alert.AlertType.WARNING);
                alert1.setTitle("");
                alert1.setHeaderText("");
                alert1.setContentText("Đơn đang được tiến hành không thể hủy!");
                alert1.getDialogPane().setPrefSize(220, 100);
                alert1.showAndWait();
                act_cancel.setDisable(true);
            } else {
                check = false;
                ResetInfor();
                RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());
            }
        }
    }

    @FXML
    void detail(ActionEvent event) {
        UserHistory selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(App.HelloApplication.class.getResource("user-detail.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setScene(scene);
            UserDetailController controller = fxmlLoader.getController();
            controller.SetDetail(selected);
            stage.show();
        }
    }

    void SetUpTable() {
        list = FXCollections.observableArrayList();
        ArrayList<UserHistory> historyList = GetHistoryDAO.getInstance().getHistory(TransferID.getID());
        for (UserHistory usH : historyList) {
            list.add(new UserHistory(usH.getRequest_id(), usH.getTime(), usH.getDate(), usH.getDeparture(), usH.getArrival(),
                    usH.getMoney(), usH.getDistance(), usH.getStatus(), usH.getDriver_name(), usH.getBrand(), usH.getSeatnumber(), usH.getLicense()));
        }
        departureTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("departure"));
        arrivalTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("arrival"));
        moneyTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("money"));
        statusTxt.setCellValueFactory(new PropertyValueFactory<UserHistory, String>("status"));
        tableView.setItems(list);
        gr3.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (CheckInput()) money.textProperty().bind(TransferMoney.strPrProperty());
        });
        TransferMoney.blPrProperty().addListener((observable, oldValue, newValue) -> {
            money.textProperty().bind(TransferMoney.strPrProperty());
        });
    }

    @FXML
    void PriceRefresh(ActionEvent event) {
        if (CheckInput()) TransferMoney.setBlPr(!TransferMoney.isBlPr());
    }

    private ObservableList<UserHistory> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        task1 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (SetRequestDAO.getInstance().CheckCustomerRequest(TransferID.getID())) break;
                }
                Platform.runLater(() -> {
                    CantCancelRequest();
                });
                RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());

                return null;
            }
        };
        task2 = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                while (true) {
                    if (SetRequestDAO.getInstance().CheckCompleteRequest(TransferID.getID())) break;
                }
                Platform.runLater(() -> {
                    CompleteRequest();
                    act_cancel.setDisable(false);
                });
                RunningStatus.setRunningStatus(!RunningStatus.isRunningStatus());

                return null;
            }
        };
        gr2.selectedToggleProperty().addListener((obsVal, oldVal, newVal) -> {
            if (newVal == null)
                oldVal.setSelected(true);
        });
        bookingView.visibleProperty().bind(btn_booking.selectedProperty());
        historyView.visibleProperty().bind(btn_history.selectedProperty());
        shipView.visibleProperty().bind(btn_ship.selectedProperty());
        foodView.visibleProperty().bind(btn_food.selectedProperty());
        time_select.disableProperty().bind(Bindings.not(time_tg.selectedProperty()));
        TransferData.statusProperty().addListener((observable, oldVal, newVal) -> {
            if (TransferData.getType() == Type.BOOKING) {
                btn_booking.setSelected(true);
                voucher.textProperty().bind(TransferData.voucherProperty());
            }
            if (TransferData.getType() == Type.SHIP) btn_ship.setSelected(true);
            if (TransferData.getType() == Type.FOOD) btn_food.setSelected(true);
        });
        SetUp();
        SetUpTable();
        RunningStatus.runningStatusProperty().addListener((observable, oldValue, newValue) -> {
            list.clear();
            SetUpTable();
        });
    }
}