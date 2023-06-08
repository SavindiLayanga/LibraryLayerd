package lk.ijse.bookworm.dto;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class FineMoney {
    private String fine_id;
    private String date;
    private String total;
    private String ReturnB_date;
}
