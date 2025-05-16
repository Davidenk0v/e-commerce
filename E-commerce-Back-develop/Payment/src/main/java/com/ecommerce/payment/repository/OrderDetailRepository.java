package com.ecommerce.payment.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.payment.dto.response.BestSellerProductDto;
import com.ecommerce.payment.entity.OrderDetail;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    
    @Query("SELECT new com.ecommerce.payment.dto.response.BestSellerProductDto(" +
            "od.name, " +
            "CAST(od.modelId AS int), " +
            "CAST(SUM(od.quantity) AS int), " +
            "SUM(od.quantity * od.price)) " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE od.sellerId = :sellerId " +
            "AND o.state = 1 " +
            "GROUP BY od.modelId, od.name " +
            "ORDER BY SUM(od.quantity * od.price) DESC")
     List<BestSellerProductDto> getBestSellingProducts(@Param("sellerId") String sellerId, Pageable pageable);
    
    @Query("SELECT FUNCTION('DATE', o.creationDate) as date, SUM(od.quantity * od.price) as total " +
            "FROM OrderDetail od " +
            "JOIN od.order o " +
            "WHERE od.sellerId = :sellerId " +
            "AND o.state = 1 " +
            "AND o.creationDate >= :startDate " +
            "GROUP BY FUNCTION('DATE', o.creationDate) " +
            "ORDER BY FUNCTION('DATE', o.creationDate)")
     List<Object[]> getSalesResult(@Param("sellerId") String sellerId, @Param("startDate") Instant startDate);
}
