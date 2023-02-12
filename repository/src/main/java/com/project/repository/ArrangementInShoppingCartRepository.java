package com.project.repository;

import com.project.domain.Arrangement;
import com.project.domain.ShoppingCart;
import com.project.domain.relations.ArrangementInShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrangementInShoppingCartRepository extends JpaRepository<ArrangementInShoppingCart, Long> {

    List<ArrangementInShoppingCart> findAllByShoppingCart(ShoppingCart shoppingCart);

}
