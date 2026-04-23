package com.easleyjs.springrpg.config;

import com.easleyjs.springrpg.entity.Item;
import com.easleyjs.springrpg.entity.ItemType;
import com.easleyjs.springrpg.repository.ItemRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/***
 * Load default items into db if db is empty.
 */
@Configuration
public class DataLoader {
    @Bean
    CommandLineRunner loadData(ItemRepo itemRepo) {
        return args -> {
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
