package com.project.repository;

import com.project.domain.Accommodation;
import com.project.domain.Arrangement;
import com.project.domain.relations.ArrangementInShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArrangementRepository extends JpaRepository<Arrangement, Long> {

    Arrangement findByArrangementInShoppingCarts(ArrangementInShoppingCart arrangementInShoppingCart);

    List<Arrangement> findAllByAccommodation(Accommodation accommodation);
}
