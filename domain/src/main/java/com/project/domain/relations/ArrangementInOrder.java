package com.project.domain.relations;

import com.project.domain.Arrangement;
import com.project.domain.Order;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
public class ArrangementInOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "arrangement_id")
    private Arrangement arrangement;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private LocalDate from_date;

    private LocalDate to_date;

    private Double price;

    public ArrangementInOrder(Arrangement arrangement,
                              Order order,
                              LocalDate from_date,
                              LocalDate to_date,
                              Double price) {
        this.arrangement = arrangement;
        this.order = order;
        this.from_date = from_date;
        this.to_date = to_date;
        this.price = price;
    }
}
