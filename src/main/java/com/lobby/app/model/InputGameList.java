package com.lobby.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class InputGameList {
    String name;
    List<Integer> idList;
}
