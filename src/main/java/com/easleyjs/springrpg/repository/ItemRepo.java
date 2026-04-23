package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemRepo extends
        JpaRepository<Item, Long> {
    Optional<Item> findByName(String name);
    Optional<Item> findByShopItem(boolean shopItem);
}
