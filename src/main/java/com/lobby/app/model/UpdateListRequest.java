package com.lobby.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UpdateListRequest {
    Integer idList;
    GameListDTO updatedList;
}
