package com.project.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.domain.identity.User;
import com.project.domain.relations.ArrangementInShoppingCart;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "shoppingCart")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "shoppingCart")
    private List<ArrangementInShoppingCart> arrangementsInShoppingCart;
}
