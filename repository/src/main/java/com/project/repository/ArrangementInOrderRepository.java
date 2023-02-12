package com.project.repository;

import com.project.domain.Order;
import com.project.domain.relations.ArrangementInOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ArrangementInOrderRepository extends JpaRepository<ArrangementInOrder, Long> {

    @Query("FROM ArrangementInOrder a WHERE a.order = :order AND a.from_date > :date")
    List<ArrangementInOrder> findByOrderAndFromDateAfter(@Param("order") Order order, @Param("date") LocalDate date);
}
