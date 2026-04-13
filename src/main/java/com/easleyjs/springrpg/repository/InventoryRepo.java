package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends
        JpaRepository<InventoryItem, Long> {}
