package com.buoobuoo.minecraftenhanced.core.block;

import com.buoobuoo.minecraftenhanced.core.block.impl.MysteryBoxBlock;
import com.buoobuoo.minecraftenhanced.core.block.impl.TitaniumOreBlock;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum CustomBlocks {

    TITANIUM_ORE(new TitaniumOreBlock()),
    MYSTERY_BOX(new MysteryBoxBlock());

    private CustomBlock handler;
    CustomBlocks(CustomBlock handler){
        this.handler = handler;
    }

    public static CustomBlock[] getHandlers(){
        List<CustomBlock> blocks = new ArrayList<>();
        for(CustomBlocks block : CustomBlocks.values()){
            blocks.add(block.getHandler());
        }
        return blocks.toArray(new CustomBlock[0]);
    }
}
