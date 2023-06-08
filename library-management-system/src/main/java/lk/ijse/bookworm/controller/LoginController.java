package lk.ijse.bookworm.controller;


import com.sun.javafx.scene.control.FakeFocusTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.bookworm.model.LoginModel;
import lk.ijse.bookworm.util.Regex;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class LoginController implements Initializable{

    @FXML
    private AnchorPane loginWindow;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private static final String VALID_USERNAME = "admin";
    private static final String VALID_PASSWORD = "admin";

    public void btnLoginOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) loginWindow.getScene().getWindow();
        stage.close();

        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"))));
        stage.setMaximized(true);
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        if (Regex.validateUsername(username)&&Regex.validatePassword(password)) {

            boolean isUserVerified ; //check in the DB
            isUserVerified = LoginModel.userCheckedInDB(username,password);
            if (isUserVerified) {

                new Alert(Alert.AlertType.CONFIRMATION, "Login successful!").showAndWait();
                stage.close();
                stage1.setMaximized(true);
                stage1.show();
            }else{
                new Alert(Alert.AlertType.WARNING, "User Not Found in DB!!!").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Invalid username or password").showAndWait();
            txtUsername.clear();
            txtPassword.clear();
            txtUsername.requestFocus();
            stage.show();
        }

    }

    public void txtUsernameOnAction(ActionEvent actionEvent) {
           txtPassword.requestFocus();
    }

    public void txtPasswordOnAction(ActionEvent event) throws IOException {
        btnLoginOnAction(event);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->txtUsername.requestFocus());
    }

    /*@FXML
    void txtPasswordOnAction(ActionEvent event) throws IOException{
        btnLoginOnAction(event);
    }

    @FXML
    void txtUsernameOnAction(ActionEvent event) {
        txtPassword.requestFocus();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->txtUsername.requestFocus());
    }*/

}
