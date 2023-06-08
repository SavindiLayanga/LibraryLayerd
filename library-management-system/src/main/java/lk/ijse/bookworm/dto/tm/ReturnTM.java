package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class ReturnTM {
    private String ReturnB_id;
    private String return_date;
    private String IssueBook_id;
    private Button btn;
}
