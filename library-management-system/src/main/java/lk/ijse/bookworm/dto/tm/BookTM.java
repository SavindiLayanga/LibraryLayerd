package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class BookTM {
    private String ISBN;
    private String Title;
    private double Price;
    private int No_Of_Pages;
    private String Pub_id;
    private int Quantity;
    private Button btn;
}
