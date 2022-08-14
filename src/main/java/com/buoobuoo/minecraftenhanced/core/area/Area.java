package com.buoobuoo.minecraftenhanced.core.area;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

@Getter
public class Area {
    protected final String name;
    protected final BoundingBox[] boundingBoxes;

    public Area(String name, BoundingBox... boundingBoxes){
        this.name = name;
        this.boundingBoxes = boundingBoxes;
    }

    public boolean contains(Location loc){
        Vector vec = loc.toVector();
        for(BoundingBox boundingBox : boundingBoxes){
            if(boundingBox.contains(vec))
                return true;
        }
        return false;
    }
}
