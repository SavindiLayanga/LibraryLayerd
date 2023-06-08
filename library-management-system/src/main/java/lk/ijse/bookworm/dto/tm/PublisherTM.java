package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class PublisherTM {
    private String Pub_id;
    private String Name;
    private String Address;
    private String Email;
    private String Contact;
    private Button btn;
}
