package com.lobby.app.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class SteamUser {
    private String steamId;
    private String username;
    private String avatar;
    private Integer communityVisibilityState;
}
