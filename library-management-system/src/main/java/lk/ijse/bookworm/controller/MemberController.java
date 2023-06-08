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
import lk.ijse.bookworm.dto.Author;
import lk.ijse.bookworm.dto.Employee;
import lk.ijse.bookworm.dto.Member;
import lk.ijse.bookworm.dto.tm.AuthorTM;
import lk.ijse.bookworm.dto.tm.MemberTM;
import lk.ijse.bookworm.model.AuthorModel;
import lk.ijse.bookworm.model.EmployeeModel;
import lk.ijse.bookworm.model.MemberModel;
import lk.ijse.bookworm.util.Regex;

import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MemberController implements Initializable {

    @FXML
    private AnchorPane WinMember;

    @FXML
    private TextField txtMemberid;

    @FXML
    private TextField txtMemContact;

    @FXML
    private TextField txtMemEmail;

    @FXML
    private TextField txtMemAddress;

    @FXML
    private TextField txtMemName;

    @FXML
    private DatePicker dateMemdt;

    @FXML
    private DatePicker dateMemExpdt;

    @FXML
    private Button btnMemberUpdate;

    @FXML
    private Button btnMemberDelete;

    @FXML
    private Button btnMemberAdd;

    @FXML
    private Button btnMemberSearch;

    @FXML
    private TableView<MemberTM> tblMember;

    @FXML
    private TableColumn<?, ?> colMemberID;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colEmail;

    @FXML
    private TableColumn<?, ?> colAddress;

    @FXML
    private TableColumn<?, ?> colContact;

    @FXML
    private TableColumn<?, ?> colMemberDate;

    @FXML
    private TableColumn<?, ?> colExpireDate;

    @FXML
    private TableColumn<?, ?> colAction;

    private String searchText="";

    private ObservableList<MemberTM> obList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setCellValueFactory();
        getAllMembersToTable(searchText);

        tblMember.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> { //Add ActionListener to selected column and display text field values
            //Check select value is not null
            if(null!=newValue) { //newValue!=null --> Get more time to compare (newValue object compare)
                // btnSaveSupplier.setText("Update Supplier");
                setDataToTextFields((MemberTM) newValue); //Set data to text field of selected row data of table
            }
        });
    }

    private void setDataToTextFields(MemberTM memberTM) {
        txtMemberid.setText(memberTM.getMem_id());
        txtMemName.setText(memberTM.getName());
        txtMemAddress.setText(memberTM.getAddress());
        txtMemEmail.setText(memberTM.getEmail());
        txtMemContact.setText(memberTM.getContact());
        dateMemdt.setValue(LocalDate.parse(memberTM.getMember_date()));
        dateMemExpdt.setValue(LocalDate.parse(memberTM.getExpire_date()));
    }

    private void getAllMembersToTable(String searchText) {
        try {
            List<Member> memberList = MemberModel.searchAll();
            for( Member member : memberList) {
                if (member.getMem_id().contains(searchText) || member.getAddress().contains(searchText)){  //Check pass text contains of the supName
                    JFXButton btnDel=new JFXButton("Delete");
                    btnDel.setAlignment(Pos.CENTER);
                    btnDel.setStyle("-fx-background-color: #686de0; ");
                    btnDel.setCursor(Cursor.HAND);

                    MemberTM tm=new MemberTM(
                            member.getMem_id(),
                            member.getName(),
                            member.getAddress(),
                            member.getEmail(),
                            member.getContact(),
                            member.getMember_date(),
                            member.getExpire_date()
                            ,btnDel);

                    obList.add(tm);

                    setDeleteButtonTableOnAction(btnDel);
                }
            }
            tblMember.setItems(obList);
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Query error!").show();
        }
    }

    private void setDeleteButtonTableOnAction(JFXButton btnDel) {
        btnDel.setOnAction((e) -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to Delete?", yes, no).showAndWait();

            if (buttonType.get() == yes) {
                txtMemberid.setText(tblMember.getSelectionModel().getSelectedItem().getMem_id());
                btnMemberSearchOnAction(e);
                btnMemberDeleteOnAction(e);
                //btnAuthorDeleteOnAction(e);

                tblMember.getItems().clear();
                getAllMembersToTable(searchText);
            }
        });
    }

    private void setCellValueFactory() {
        colMemberID.setCellValueFactory(new PropertyValueFactory<>("Mem_id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colContact.setCellValueFactory(new PropertyValueFactory<>("Contact"));
        colMemberDate.setCellValueFactory(new PropertyValueFactory<>("member_date"));
        colExpireDate.setCellValueFactory(new PropertyValueFactory<>("expire_date"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("btn"));
    }


    //**************************************************


    @FXML
    void btnMemberAddOnAction(ActionEvent event) throws SQLException {
        String Mem_id= txtMemberid.getText();
        String Name = txtMemName.getText();
        String address = txtMemAddress.getText();
        String Email = txtMemEmail.getText();
        String Contact = txtMemContact.getText();
        String member_date = String.valueOf(dateMemdt.getValue());
        String expire_date = String.valueOf(dateMemExpdt.getValue());

        boolean isSaved = MemberModel.add(Mem_id, Name, address, Email, Contact, member_date, expire_date);
        if (isSaved) {
            new Alert(Alert.AlertType.CONFIRMATION, "Member Added!!!").show();
            tblMember.getItems().clear();
            getAllMembersToTable(searchText);
        }else {
            new Alert(Alert.AlertType.ERROR, "ERROR").show();
        }
    }

    @FXML
    void btnMemberSearchOnAction(ActionEvent event) {
        String Mem_id = txtMemberid.getText();
        try {
            Member member = MemberModel.search(Mem_id);
            if (member!= null) {
                txtMemberid.setText(member.getMem_id());
                txtMemName.setText(member.getName());
                txtMemAddress.setText(member.getAddress());
                txtMemEmail.setText(member.getEmail());
                txtMemContact.setText(member.getContact());
                dateMemdt.setValue(LocalDate.parse((CharSequence) member.getMember_date()));
                dateMemExpdt.setValue(LocalDate.parse((CharSequence) member.getExpire_date()));

            } else {
                new Alert(Alert.AlertType.WARNING, "no member found :(").show();
            }
        } catch (SQLException e) {
            System.out.println(e);
            //new Alert(Alert.AlertType.ERROR, "oops! something went wrong :(").show();
        }
    }

    @FXML
    void btnMemberUpdateOnAction(ActionEvent event) {
        String Mem_id = txtMemberid.getText();
        String Name = txtMemName.getText();
        String address = txtMemAddress.getText();
        String Email = txtMemEmail.getText();
        String Contact = txtMemContact.getText();
        String member_date = String.valueOf(dateMemdt.getValue());
        String expire_date = String.valueOf(dateMemExpdt.getValue());

        var member = new Member(Mem_id, Name, address, Email, Contact, member_date, expire_date);   //type inference

        try {
            boolean isUpdated = MemberModel.update(member);
            if (isUpdated) {
                new Alert(Alert.AlertType.CONFIRMATION, " Member Updated!").show();
            }
        } catch (SQLException e) {
            System.out.println(e);
            //new Alert(Alert.AlertType.ERROR, "SQL Error!").show();
        }
    }

    @FXML
    void btnMemberDeleteOnAction(ActionEvent event) {
        String MemberID= txtMemberid.getText();

        try {
            boolean isDeleted = MemberModel.delete(MemberID);
            if (isDeleted) {
                new Alert(Alert.AlertType.CONFIRMATION, "Member deleted !").show();
            }
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "something happened !").show();
        }
    }

    //***************************************

    @FXML
    void txtMemberidOnAction(ActionEvent event) {
        String Mem_Id= txtMemberid.getText();
        if(Regex.validateMem_Id(Mem_Id)){
            txtMemName.requestFocus();

        }else {
            new Alert(Alert.AlertType.WARNING, "No Matching ID!!!").show();
        }
    }

    @FXML
    void txtMemContactOnAction(ActionEvent event) {
        String contact=txtMemContact.getText();
        if (Regex.validateMemContact(contact)){
            dateMemdt.requestFocus();
        }else {
            txtMemContact.clear();
            new Alert(Alert.AlertType.WARNING, "No matching contact number please Input SUP format!!!").show();
        }
    }
}

