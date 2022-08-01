package com.buoobuoo.minecraftenhanced.core.util.unicode;

import lombok.Getter;

@Getter
public enum CharRepo {

    RANK_ADMIN_TAG("\uE001"),
    RANK_DEVELOPER_TAG("\uE002"),

    UI_DIALOGUE_BORDER("\uF002"),
    UI_PORTRAIT_BOUNCER_TUFF("\uF003"),

    UI_INVENTORY_DEFAULT_CRATE_OPEN("\uF004"),
    UI_INVENTORY_PLAYER_SEARCH_OVERLAY("\uF005"),

    UI_INVENTORY_PARTY_DECO_1("\uF009"),
    UI_INVENTORY_PARTY_MAIN_MEMBER("\uF006"),
    UI_INVENTORY_PARTY_MAIN_LEADER("\uF007"),
    UI_INVENTORY_PARTY_EMPTY("\uF008");

    private String ch;
    CharRepo(String ch){
        this.ch = ch;
    }

    @Override
    public String toString(){
        return ch;
    }

}
