package com.buoobuoo.minecraftenhanced.core.item;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.util.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class CustomItem implements Listener {
    protected CustomItemManager manager;

    protected String id;
    protected String displayName;
    protected String[] lore;
    protected Material material;
    protected int customModelData;

    protected ItemRarity rarity = ItemRarity.COMMON;

    public void addManager(CustomItemManager manager){
        this.manager = manager;
    }

    public CustomItem(String id, Material material, String displayName, String... lore){
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
    }

    public CustomItem(String id, Material material, int customModelData, String displayName, String... lore){
        this.id = id;
        this.material = material;
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = customModelData;
    }

    public CustomItem(String id, MatRepo mat, String displayName, String... lore){
        this.id = id;
        this.material = mat.getMat();
        this.displayName = displayName;
        this.lore = lore;
        this.customModelData = mat.getCustomModelData();
    }

    protected boolean isApplicable(ItemStack item){
        return this.manager.getRegistry().isApplicable(this, item);
    }

    public void onCreate(MinecraftEnhanced plugin, ItemBuilder ib){
        ib.lore(lore);
        ib.lore(10, "&r&f" + rarity.getIcon().getCh());
    }
}
