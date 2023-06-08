package lk.ijse.bookworm.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString

public class Book {
    private String ISBN;
    private String Title;
    private double Price;
    private int No_Of_Pages;
    private String Pub_id;
    private int Quantity;
}
