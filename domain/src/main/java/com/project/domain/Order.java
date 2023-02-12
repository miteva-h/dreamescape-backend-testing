package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInOrder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "order")
    private List<ArrangementInOrder> arrangementsInOrder;

    private Double totalCost;

    public Order(User user) {
        this.user = user;
    }
}
