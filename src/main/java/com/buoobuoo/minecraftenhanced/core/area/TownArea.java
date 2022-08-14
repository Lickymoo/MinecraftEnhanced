package com.buoobuoo.minecraftenhanced.core.area;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;

@Getter
public class TownArea extends Area{
    protected final Location respawnPoint;

    public TownArea(String name, Location respawnPoint, BoundingBox... boundingBoxes){
        super(name, boundingBoxes);
        this.respawnPoint = respawnPoint;
    }
}
