package com.ecommerce.cart.repositories;

import com.ecommerce.cart.entities.Wanted;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WantedRepository extends JpaRepository<Wanted, Long> {
    List<Wanted> findByUserId(String userId);
    Optional<Wanted> findByUserIdAndModelId(String userId, Long modelId);
}
