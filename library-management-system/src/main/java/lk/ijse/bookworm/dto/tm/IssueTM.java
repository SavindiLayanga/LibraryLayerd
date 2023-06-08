package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class IssueTM {
    private String Issue_id;
    private String issue_date;
    private String isbn;
    private String Mem_id;
    private String returnDate;
    private Button btn;
}
