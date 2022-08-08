package com.buoobuoo.minecraftenhanced.core.chat;

import lombok.Getter;

import java.util.Locale;

@Getter
public enum ChatContext {
    GLOBAL(""),
    STAFF("@s"),
    PARTY("@p");

    private final String prefix;
    ChatContext(String prefix){
        this.prefix = prefix;
    }

    public static ChatContext matchContext(String prefix){
        return switch (prefix.toLowerCase(Locale.ROOT)){
            case "@s", "@staff" -> STAFF;
            case "@p", "@party" -> PARTY;
            default -> GLOBAL;
        };
    }
}
