package lk.ijse.bookworm.dto.tm;

import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor

public class MemberTM {
        private String Mem_id;
        private String Name;
        private String address;
        private String Email;
        private String Contact;
        private String member_date;
        private String expire_date;
        private Button btn;
}

