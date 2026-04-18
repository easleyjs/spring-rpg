package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.InventoryItem;
import com.easleyjs.springrpg.entity.ItemType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryRepo extends
        JpaRepository<InventoryItem, Long> {
    public List<InventoryItem> findAllByPlayerId(long pcId);
    public Optional<InventoryItem> findByPlayerIdAndEquippedTrueAndItemType(
            Long playerId,
            ItemType type
    );
}
