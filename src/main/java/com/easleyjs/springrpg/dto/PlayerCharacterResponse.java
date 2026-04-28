package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.Location;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerCharacterResponse {
    String name;
    long xp;
    long level;
    long health;
    Location location;
    long gold;
}
