package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetails {
    private Integer id;
    private Order order;
    private double amount;
    private Product product;
    private int quantity;
}
