package lk.ijse.bookworm.dto;

import lombok.*;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data

public class Member {
    private String Mem_id;
    private String Name;
    private String address;
    private String Email;
    private String Contact;
    private String member_date;
    private String expire_date;
}

