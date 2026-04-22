package com.easleyjs.springrpg.dto;

import com.easleyjs.springrpg.entity.Location;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MoveRequest {
    private long pcId;
    private Location location;
}
