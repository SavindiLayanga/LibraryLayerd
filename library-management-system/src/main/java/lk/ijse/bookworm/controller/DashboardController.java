package lk.ijse.bookworm.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {
    public javafx.scene.control.Label lblDate;
    public Label lblTime;
    public Label lblGreet;
    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private Button btnLogOut;

    public void btnDashboardOnActhion(ActionEvent actionEvent) throws IOException {
        Stage thisStage = (Stage) ((Node)actionEvent.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader= new FXMLLoader(LoginController.class.getResource("/view/dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        thisStage.setScene(scene);
        thisStage.setMaximized(true);

    }

    public void btnEmployeeOnActhion(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/employee.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnMembersOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/members.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/employee.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnSuppliersOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/publisher.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnIssueBookOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/issue-book.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnBookOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/book.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnReturnBookOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/return-book.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnFineMoneyOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/fine-money-manage.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

    }

    public void btnLogOutOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/employee.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);

        Stage stage = (Stage) dashboardPane.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/login.fxml"))));

        stage.centerOnScreen();
        stage.show();

    }

    public void btnAuthorOnAction(ActionEvent actionEvent) throws IOException {
        Parent load=FXMLLoader.load(getClass().getResource("/view/authors.fxml"));
        dashboardPane.getChildren().clear();
        dashboardPane.getChildren().add(load);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        generateDate();
        generateTime();
        lblGreetOnAction();

    }

    private void generateTime() {
        lblTime.setText(String.valueOf(LocalTime.now()));
    }

    private void generateDate() {
        lblDate.setText(String.valueOf(LocalDate.now()));
    }


    public void lblGreetOnAction() {
            Timeline time = new Timeline(new KeyFrame(Duration.ZERO, e -> {
                LocalTime currentTime = LocalTime.now();
                if (currentTime.getHour() > 6 && currentTime.getHour() < 12) {
                    lblGreet.setText("Good Morning");
                } else if (currentTime.getHour() >= 12 && currentTime.getHour() < 16) {
                    lblGreet.setText("Good AfterNoon");
                } else if (currentTime.getHour() >= 16 && currentTime.getHour() < 19) {
                    lblGreet.setText("Good Evening");
                } else {
                    lblGreet.setText("Good Night");
                }
            }), new KeyFrame(Duration.seconds(1)));
            time.setCycleCount(Animation.INDEFINITE);
            time.play();
    }
}

