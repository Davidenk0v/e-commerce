package com.ecommerce.payment.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.payment.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

	boolean existsByUserIdAndAddressAlias(String userId, String addressAlias);

	Optional<Address> findByUserIdAndAddressAlias(String userId, String addressAlias);

	boolean existsByUserIdAndAddressAliasAndIdNot(String userId, String addressAlias, Long id);

	List<Address> findByUserId(String userId);
}
