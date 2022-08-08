package com.buoobuoo.minecraftenhanced.core.util.unicode;

import lombok.Getter;

@Getter
public enum CharRepo {

    RANK_ADMIN_TAG("\uE001"),
    RANK_DEVELOPER_TAG("\uE002"),

    RARITY_COMMON("\uE011"),
    RARITY_MAGIC("\uE012"),
    RARITY_RARE("\uE013"),
    RARITY_ULTRA_RARE("\uE014"),

    TAG_NUM_E("\uF110"),
    TAG_NUM_0("\uF100"),
    TAG_NUM_1("\uF101"),
    TAG_NUM_2("\uF102"),
    TAG_NUM_3("\uF103"),
    TAG_NUM_4("\uF104"),
    TAG_NUM_5("\uF105"),
    TAG_NUM_6("\uF106"),
    TAG_NUM_7("\uF107"),
    TAG_NUM_8("\uF108"),
    TAG_NUM_9("\uF109"),

    TAG_LEVEL_UP("\uE015"),

    HEART("\uE016"),
    SPEECH("\uF001"),

    MANA_BALL_FULL("\uF210"),
    MANA_BALL_EMPTY("\uF220"),
    MANA_BALL_HALF("\uF230"),

    STATUS_EFFECT_FLAGELLANT("\uF331"),
    STATUS_EFFECT_BULWARK("\uF332"),

    UI_DIALOGUE_BORDER("\uF002"),
    UI_PORTRAIT_BOUNCER_TUFF("\uF003"),
    UI_PORTRAIT_CAPTAIN_YVES("\uF003"),

    UI_INVENTORY_DEFAULT_CRATE_OPEN("\uF004"),
    UI_INVENTORY_PLAYER_SEARCH_OVERLAY("\uF005"),

    UI_INVENTORY_PARTY_DECO_1("\uF009"),
    UI_INVENTORY_PARTY_MAIN_MEMBER("\uF006"),
    UI_INVENTORY_PARTY_MAIN_LEADER("\uF007"),
    UI_INVENTORY_PARTY_EMPTY("\uF008"),

    UI_INVENTORY_PROFILE_SELECT_MAIN("\uF010"),
    UI_INVENTORY_PROFILE_SELECT_FULL("\uF013"),
    UI_INVENTORY_PROFILE_EDIT_MAIN("\uF011"),
    UI_INVENTORY_PROFILE_ICON_EDIT("\uF012"),
    UI_INVENTORY_PROFILE_CONFIRM_DELETE("\uF015"),

    UI_INVENTORY_LOGOUT("\uF014"),

    UI_INVENTORY_PLAYERMENU_ABILITIES("\uF016"),
    UI_INVENTORY_PLAYERMENU_ABILITY_CASTTYPE("\uF017");

    private String ch;
    CharRepo(String ch){
        this.ch = ch;
    }

    @Override
    public String toString(){
        return ch;
    }

    public static CharRepo numAsString(char c){
        switch (c){
            case '1':
                return TAG_NUM_1;
            case '2':
                return TAG_NUM_2;
            case '3':
                return TAG_NUM_3;
            case '4':
                return TAG_NUM_4;
            case '5':
                return TAG_NUM_5;
            case '6':
                return TAG_NUM_6;
            case '7':
                return TAG_NUM_7;
            case '8':
                return TAG_NUM_8;
            case '9':
                return TAG_NUM_9;
            default:
                return TAG_NUM_0;

        }
    }

    public static String numToTagString(int n){
        String str = Integer.toString(n);
        StringBuilder sb = new StringBuilder();

        sb.append(TAG_NUM_E);
        for(char c : str.toCharArray()){
            sb.append(UnicodeSpaceUtil.getNeg(1) + numAsString(c));
        }

        return sb.toString();
    }

}
