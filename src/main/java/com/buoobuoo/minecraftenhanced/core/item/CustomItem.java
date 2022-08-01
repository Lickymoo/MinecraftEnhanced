package com.buoobuoo.minecraftenhanced.core.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

@Getter
public class CustomItem implements Listener {
    protected CustomItemManager manager;

    private String id;
    private String displayName;
    private String[] lore;
    private Material material;
    private int customModelData;

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

    protected boolean isApplicable(ItemStack item){
        return this.manager.getRegistry().isApplicable(this, item);
    }
}
