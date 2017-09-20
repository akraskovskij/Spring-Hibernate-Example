package by.kraskovski.pms.controller.dto;

import by.kraskovski.pms.domain.model.CartProductStock;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
public class CartDto {

    private String id;
    private double totalCost;
    private Set<CartProductStock> cartProductStocks;
    private LocalDateTime createDate;
}
