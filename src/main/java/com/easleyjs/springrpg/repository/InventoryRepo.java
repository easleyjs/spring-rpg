package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepo extends
        JpaRepository<InventoryItem, Long> {
    public List<InventoryItem> findByPlayerId(long pcId);
}
