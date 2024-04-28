package com.example.xujie.repository.mysql;

import com.example.xujie.entity.mysql.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.sql.Timestamp;


@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {

@Query("SELECT o FROM Orders o " +
        "WHERE (:orderNumber IS NULL OR o.orderNumber = :orderNumber) " +
        "AND (:productName IS NULL OR o.productName = :productName) " +
        "AND (:purchaseDate IS NULL OR DATE(o.purchaseDate) = :purchaseDate)")
    Page<Orders> searchOrders(String orderNumber,
                              String productName,
                              Timestamp purchaseDate,
                              Pageable pageable);
}
