package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.core.item.impl.*;
import com.buoobuoo.minecraftenhanced.core.item.impl.quest.act1.ACT1_MQ2_NecklaceItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.IceSwordItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.TestBowItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter.StarterAxeItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter.StarterBowItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter.StarterStaffItem;
import com.buoobuoo.minecraftenhanced.core.item.impl.weapon.starter.StarterSwordItem;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CustomItems {

    TITANIUM_ORE(new TitaniumOreItem()),
    TITANIUM_INGOT(new TitaniumIngotItem()),
    MYSTERY_BOX(new MysteryBoxItem()),

    //starter
    STARTER_STAFF(new StarterStaffItem()),
    STARTER_BOW(new StarterBowItem()),
    STARTER_SWORD(new StarterSwordItem()),
    STARTER_AXE(new StarterAxeItem()),

    //misc weapons
    TEST_BOW(new TestBowItem()),
    ICE_SWORD(new IceSwordItem()),

    //misc armour
    BUBYS_HELMET(new BubysHelmetItem()),

    //other
    ABILITY_GEM(new AbilityGemItem(null)),

    //quest
    ACT1_MQ3_NECKLACE(new ACT1_MQ2_NecklaceItem());

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
