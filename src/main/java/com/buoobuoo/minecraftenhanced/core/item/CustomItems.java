package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.core.item.impl.IceSwordItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.MysteryBoxItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.TitaniumIngotItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.TitaniumOreItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CustomItems {

    TITANIUM_ORE(new TitaniumOreItem()),
    TITANIUM_INGOT(new TitaniumIngotItem()),
    MYSTERY_BOX(new MysteryBoxItem()),
    ICE_SWORD(new IceSwordItem());

    private CustomItem handler;
    CustomItems(CustomItem handler){
        this.handler = handler;
    }

    public static CustomItem[] getHandlers(){
        List<CustomItem> items = new ArrayList<>();
        for(CustomItems item : CustomItems.values()){
            items.add(item.getHandler());
        }
        return items.toArray(new CustomItem[0]);
    }
}
