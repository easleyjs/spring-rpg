package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.Location;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PlayerMoveResponse {
    long pcId;
    Location location;

}
