package lk.ijse.bookworm.model;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javax.swing.text.html.ImageView;

public class FineMoneyModel {

    @FXML
    private ImageView WinFineMoney;

    @FXML
    private TextField txtFineId;

    @FXML
    private TextField txtTotal;

    @FXML
    private DatePicker dtDate;

    @FXML
    private TableView<?> tblFineMoney;

    @FXML
    private TableColumn<?, ?> colFineId;

    @FXML
    private TableColumn<?, ?> colReturnId;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colTotal;

    @FXML
    private TableColumn<?, ?> colAction;

    @FXML
    private ComboBox<?> cmbReturnId;

    @FXML
    private Button btnAddRecord;

    @FXML
    void btnAddRecordOnAction(ActionEvent event) {

    }

    @FXML
    void txtFineIdOnAction(ActionEvent event) {

    }

}
