package com.buoobuoo.minecraftenhanced.core.util.unicode;

import lombok.Getter;

@Getter
public enum CharRepo {

    //MISC ICONS
    HEART("\uE001"),
    SPEECH("\uE002"),
    MANA_BALL_FULL("\uE003"),
    MANA_BALL_EMPTY("\uE004"),
    MANA_BALL_HALF("\uE005"),

    //TAGS
    TAG_NUM_E("\uE401"),
    TAG_NUM_0("\uE402"),
    TAG_NUM_1("\uE403"),
    TAG_NUM_2("\uE404"),
    TAG_NUM_3("\uE405"),
    TAG_NUM_4("\uE406"),
    TAG_NUM_5("\uE407"),
    TAG_NUM_6("\uE408"),
    TAG_NUM_7("\uE409"),
    TAG_NUM_8("\uE40A"),
    TAG_NUM_9("\uE40B"),
    TAG_LEVEL_UP("\uE40C"),

    RANK_ADMIN_TAG("\uE40D"),
    RANK_DEVELOPER_TAG("\uE40E"),

    RARITY_COMMON("\uE415"),
    RARITY_UNCOMMON("\uE416"),
    RARITY_RARE("\uE417"),
    RARITY_ULTRA_RARE("\uE418"),
    RARITY_ULTRA_UNIQUE("\uE419"),

    //STATUS EFFECT ICONS
    STATUS_EFFECT_FLAGELLANT("\uEC01"),
    STATUS_EFFECT_BULWARK("\uEC02"),

    //DIALOGUE PORTRAITS
    UI_DIALOGUE_BORDER("\uF001"),
    UI_PORTRAIT_QUEST_STARTED("\uF002"),
    UI_PORTRAIT_QUEST_COMPLETE("\uF003"),
    UI_PORTRAIT_QUEST_CHECKPOINT("\uF004"),
    UI_PORTRAIT_CAPTAIN_YVES("\uF005"),
    UI_PORTRAIT_ARAMORE_BLACKSMITH("\uF006"),
    UI_PORTRAIT_JAYCE("\uF007"),

    //INVENTORY OVERLAYS
    UI_INVENTORY_DEFAULT_CRATE_OPEN("\uF401"),
    UI_INVENTORY_PLAYER_SEARCH_OVERLAY("\uF402"),

    UI_INVENTORY_PARTY_DECO_1("\uF406"),
    UI_INVENTORY_PARTY_MAIN_MEMBER("\uF403"),
    UI_INVENTORY_PARTY_MAIN_LEADER("\uF404"),
    UI_INVENTORY_PARTY_EMPTY("\uF405"),

    UI_INVENTORY_PROFILE_SELECT_MAIN("\uF407"),
    UI_INVENTORY_PROFILE_SELECT_FULL("\uF408"),
    UI_INVENTORY_PROFILE_EDIT_MAIN("\uF409"),
    UI_INVENTORY_PROFILE_ICON_EDIT("\uF40A"),
    UI_INVENTORY_PROFILE_CONFIRM_DELETE("\uF40B"),

    UI_INVENTORY_LOGOUT("\uF40C"),

    UI_INVENTORY_PLAYERMENU_ABILITIES("\uF40D"),
    UI_INVENTORY_PLAYERMENU_ABILITY_CASTTYPE("\uF40E"),
    UI_INVENTORY_PLAYERMENU_SELF_MAIN("\uF40F"),

    UI_INVENTORY_PLAYERMENU_HELMET_ICON("\uF411"),
    UI_INVENTORY_PLAYERMENU_CHESTPLATE_ICON("\uF412"),
    UI_INVENTORY_PLAYERMENU_LEGGINGS_ICON("\uF413"),
    UI_INVENTORY_PLAYERMENU_BOOTS_ICON("\uF414");

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
