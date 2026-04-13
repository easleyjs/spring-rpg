package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends
        JpaRepository<Item, Long> {}
