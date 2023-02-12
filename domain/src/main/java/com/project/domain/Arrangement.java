package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.relations.ArrangementInOrder;
import com.project.domain.relations.ArrangementInShoppingCart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Arrangement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate from_date;

    private LocalDate to_date;

    @ManyToOne
    @JoinColumn(name = "accommodation_id", nullable = false)
    private Accommodation accommodation;

    @JsonIgnore
    @OneToMany(mappedBy = "arrangement")
    private List<ArrangementInShoppingCart> arrangementInShoppingCarts;

    @JsonIgnore
    @OneToMany(mappedBy = "arrangement")
    private List<ArrangementInOrder> arrangementInOrders;

    public Arrangement(LocalDate from_date,
                       LocalDate to_date,
                       Accommodation accommodation) {
        this.from_date = from_date;
        this.to_date = to_date;
        this.accommodation = accommodation;
    }

    public List<LocalDate> getDatesBetween() {
        List<LocalDate> newList = new ArrayList<>();
        LocalDate start = from_date.plusDays(1);
        LocalDate end = to_date.minusDays(1);
        while (!start.isAfter(end)) {
            newList.add(start);
            start = start.plusDays(1);
        }
        return newList;
    }
}
