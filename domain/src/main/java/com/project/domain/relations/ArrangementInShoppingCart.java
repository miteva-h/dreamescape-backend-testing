package com.project.domain.relations;

import com.project.domain.Arrangement;
import com.project.domain.ShoppingCart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class ArrangementInShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "arrangement_id")
    private Arrangement arrangement;

    @ManyToOne
    @JoinColumn(name = "shoppingCart_id")
    private ShoppingCart shoppingCart;

    private LocalDate from_date;

    private LocalDate to_date;

    private Double price;

    public ArrangementInShoppingCart(Arrangement arrangement,
                                     ShoppingCart shoppingCart,
                                     LocalDate from_date,
                                     LocalDate to_date,
                                     Double price) {
        this.arrangement = arrangement;
        this.shoppingCart = shoppingCart;
        this.from_date = from_date;
        this.to_date = to_date;
        this.price = price;
    }
}
