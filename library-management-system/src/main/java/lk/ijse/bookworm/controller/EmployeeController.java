package lk.ijse.bookworm.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.tm.EmployeeTM;
import lk.ijse.bookworm.model.EmployeeModel;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable {

    @FXML
    private AnchorPane WinEmployee;

    @FXML
    private TextField txtEmployeeId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtJobRole;

    @FXML
    private TextField txtContact;

    @FXML
    private Button btnEmployeeAdd;

    @FXML
    private Button btnEmployeeUpdate;

    @FXML
    private Button btnEmployeeDelete;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnEmployeeSearch;

    @FXML
    private TableView<EmployeeTM> tblEmployee;

    @FXML
    private TableColumn<?, ?> colEmployeeID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colJobRole;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colAction;

    private String searchText="";

    private ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url,ResourceBundle resourceBundle){
        setCellValueFactory();
        getAllEmployeeToTable(searchText);
        //setDeleteButtonTableOnAction();

        tblEmployee.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)

                setDataToTextFields((EmployeeTM) newValue); //Set data to text field of selected row data of table
            }
        });

    }

    private void setDataToTextFields(EmployeeTM employeeTM) {
        txtEmployeeId.setText(employeeTM.getEmp_id());
        txtName.setText(employeeTM.getName());
        txtEmail.setText(String.valueOf(employeeTM.getEmail()));
        txtContact.setText(employeeTM.getContact());
        txtJobRole.setText(String.valueOf(employeeTM.getJob_Role()));
    }

    private void getAllEmployeeToTable(String searchText) {
        try {
            List<Employee> employeeList = EmployeeModel.searchAll();
            for( Employee employee : employeeList) {
                if (employee.getEmp_id().contains(searchText) || employee.getName().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    EmployeeTM tm=new EmployeeTM(
                            employee.getEmp_id(),
                            employee.getName(),
                            employee.getEmail(),
                            employee.getContact(),
                            employee.getJob_Role()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblEmployee.setItems(obList);
        } catch (SQLException e) {
            System.out.println(e);
            //  e.printStackTrace();
            //   new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setDeleteButtonTableOnAction(JFXButton btnDel) {
        btnDel.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete?", yes, no).showAndWait();

            if (buttonType.get() == yes) {
                txtEmployeeId.setText(tblEmployee.getSelectionModel().getSelectedItem().getEmp_id());
                btnEmployeeSearchOnAction(e);
                btnEmployeeDeleteOnAction(e);
                btnEmployeeDeleteOnAction(e);

                tblEmployee.getItems().clear();
                getAllEmployeeToTable(searchText);
            }
        });
    }
    private void setCellValueFactory() {
        colEmployeeID.setCellValueFactory(new PropertyValueFactory<>("Emp_id")); //SupplierTM class attributes names
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colJobRole.setCellValueFactory(new PropertyValueFactory<>("Job_Role"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }

    //*******************************************

    @FXML
    void btnEmployeeAddOnAction(ActionEvent event) throws SQLException {
        String Emp_id = txtEmployeeId.getText();
        String Name = txtName.getText();
        String Email = txtEmail.getText();
        String Contact = txtContact.getText();
        String Job_Role = txtJobRole.getText();

        boolean isSaved = EmployeeModel.add(Emp_id, Name, Email, Contact, Job_Role);
        if (isSaved) {
            new Alert(Alert.AlertType.CONFIRMATION, "Employee Added!!!").show();
            tblEmployee.getItems().clear();
            getAllEmployeeToTable(searchText);
        } else {
            new Alert(Alert.AlertType.ERROR, "error !!!").show();
        }
        txtEmployeeId.clear();
        txtName.clear();
        txtEmail.clear();
        txtContact.clear();
        txtJobRole.clear();
    }

    @FXML
    void btnEmployeeSearchOnAction(ActionEvent event) {
        String Emp_id = txtEmployeeId.getText();
        try {

            Employee employee = EmployeeModel.search(Emp_id);
            if (employee != null) {
                txtEmployeeId.setText(employee.getEmp_id());
                txtName.setText(employee.getName());
                txtEmail.setText(String.valueOf(employee.getEmail()));
                txtContact.setText(String.valueOf(employee.getContact()));
                txtJobRole.setText(String.valueOf(employee.getJob_Role()));
            } else {
                new Alert(Alert.AlertType.WARNING, "no employee found :(").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void btnEmployeeDeleteOnAction(ActionEvent actionEvent) {
        String Emp_id = txtEmployeeId.getText();

        try {
            boolean isDeleted = EmployeeModel.delete(Emp_id);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Employee deleted !").show();
            }
        } catch (SQLException e) {
            //System.out.println(e);
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
    }

    //*******************************************

    @FXML
    void btnEmployeeUpdateOnAction(ActionEvent event) {
        String Emp_Id = txtEmployeeId.getText();
        String Name = txtName.getText();
        String Email = txtEmail.getText();
        String Contact = txtContact.getText();
        String Job_Role = txtJobRole.getText();

        var employee = new Employee(Emp_Id, Name, Email, Contact, Job_Role);   //type inference

        try {
            boolean isUpdated = EmployeeModel.update(employee);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, " Employee Updated!").show();
            }
        } catch (SQLException e) {
            //System.out.println(e);
            new Alert(Alert.AlertType.ERROR, "Error!").show();
        }
    }

    public void txtEmployeeIdOnAction(ActionEvent actionEvent) {
        String Emp_id= txtEmployeeId.getText();
        if(Regex.validateEmployeeId(Emp_id)){
            txtName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

    @FXML
    void txtContactOnAction(ActionEvent event) {
        String contact=txtContact.getText();
        if (Regex.validateContact(contact)){
            //  btnSaveOnAction(actionEvent);
            txtJobRole.requestFocus();
        }else {
            txtContact.clear();
            new Alert(Alert.AlertType.WARNING, "No matching contact number please Input SUP format!!!").show();
        }
    }

    public void txtNameOnAction(ActionEvent actionEvent) {
    }

    public void txtJobRoleOnAction(ActionEvent actionEvent) {
    }

    public void txtEmailOnAction(ActionEvent actionEvent) {
    }
}