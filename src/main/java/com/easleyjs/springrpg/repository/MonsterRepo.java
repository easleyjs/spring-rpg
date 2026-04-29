package com.easleyjs.springrpg.repository;

import com.easleyjs.springrpg.entity.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonsterRepo
        extends JpaRepository<Monster, String> {
    List<Monster> findByMinLevelLessThanEqualAndMaxLevelGreaterThanEqual(int level1, int level2);
}
