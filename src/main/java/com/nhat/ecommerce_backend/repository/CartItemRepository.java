package com.nhat.ecommerce_backend.repository;

import com.nhat.ecommerce_backend.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product WHERE ci.cart.id = :cartId")
    List<CartItem> findAllByCartId(UUID cartId);

    Optional<CartItem> findByCartIdAndProductId(UUID id, Long id1);
}
