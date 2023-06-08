package lk.ijse.bookworm.dto.tm;

import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor

public class FineMoneyTM {
    private String fine_id;
    private String date;
    private int total;
    private String ReturnB_id;
    private Button btn;
}
