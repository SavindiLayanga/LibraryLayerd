package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class EmployeeTM {
    private String Emp_id;
    private String Name;
    private String Email;
    private String Contact;
    private String Job_Role;
    private Button btn;
}
