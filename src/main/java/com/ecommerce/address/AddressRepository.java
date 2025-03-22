package com.ecommerce.address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    @Override
    @NonNull
    @Query("SELECT e FROM Address e WHERE e.isActive = true ORDER BY e.id ASC")
    List<Address> findAll();

    @Query("SELECT e FROM Address e ORDER BY e.id ASC")
    List<Address> findAllIncludingInactive();

    @Query("SELECT e FROM Address e WHERE e.isActive = false ORDER BY e.id ASC")
    List<Address> findAllInactive();
}
