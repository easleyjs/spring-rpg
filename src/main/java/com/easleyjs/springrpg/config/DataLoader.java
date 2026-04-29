package com.easleyjs.springrpg.config;

import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.ItemType;
import com.easleyjs.springrpg.entity.Monster;
import com.easleyjs.springrpg.repository.ItemRepo;
import com.easleyjs.springrpg.repository.MonsterRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * Load default items into db if db is empty.
 */
@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(ItemRepo itemRepo, MonsterRepo monsterRepo) {
        return args -> {
            if (monsterRepo.count() == 0) {
                monsterRepo.save(new Monster(
                        "Large Rat",
                        5,
                        1,
                        2,
                        5,
                        2,
                        0
                ));
                monsterRepo.save(new Monster(
                        "Wild Boar",
                        20,
                        1,
                        2,
                        10,
                        4,
                        0
                ));
                monsterRepo.save(new Monster(
                        "Undead Mole",
                        30,
                        2,
                        3,
                        10,
                        6,
                        0
                ));
                monsterRepo.save(new Monster(
                        "Skeleton",
                        40,
                        2,
                        3,
                        10,
                        10,
                        5
                ));
            }
            if (itemRepo.count() == 0) {
                itemRepo.save(new Item(
                        "Wooden Stick",
                        "A firm-looking wooden stick",
                        0,
                        1,
                        0,
                        0,
                        ItemType.WEAPON,
                        false
                ));
                itemRepo.save(new Item(
                        "Peasant Outfit",
                        "The garb of a regular peasant",
                        0,
                        0,
                        0,
                        0,
                        ItemType.ARMOR,
                        false
                ));
                itemRepo.save(new Item(
                        "Wooden Sword",
                        "Deadlier than a stick",
                        2,
                        1,
                        0,
                        10,
                        ItemType.WEAPON,
                        false
                ));
                itemRepo.save(new Item(
                        "Iron Sword",
                        "A modest iron sword",
                        5,
                        1,
                        0,
                        25,
                        ItemType.WEAPON,
                        false
                ));
            }
        };
    }
}
