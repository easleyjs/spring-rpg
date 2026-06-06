package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepo extends
        JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
    Page<Item> findByShopItemTrue(Pageable pageable);
}
