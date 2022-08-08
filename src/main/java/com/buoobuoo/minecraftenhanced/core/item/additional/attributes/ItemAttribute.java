package com.buoobuoo.minecraftenhanced.core.item.additional.attributes;

import com.buoobuoo.minecraftenhanced.core.damage.DamageInstance;
import com.buoobuoo.minecraftenhanced.core.player.ProfileStatInstance;
import com.buoobuoo.minecraftenhanced.core.util.Pair;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.Listener;

@Getter
@Setter
public abstract class ItemAttribute implements Listener {
    protected final String name;
    protected final String id;
    protected final Pair<Double, Double> minMaxRoll;

    protected boolean roundValue = false;

    public ItemAttribute(String name, String id, double minRoll, double maxRoll){
        this.name = name;
        this.id = id;
        this.minMaxRoll = Pair.of(minRoll, maxRoll);
    }

    public abstract String itemLore(ItemAttributeInstance instance);

    public double roll(){
        double val = Util.randomDouble(minMaxRoll.getLeft(), minMaxRoll.getRight());
        if(roundValue)
            val = Math.round(val);
        return  val;
    }

    public abstract void onDamage(DamageInstance damageInstance, ItemAttributeInstance instance);

    public abstract void onCalc(ProfileStatInstance statInstance, ItemAttributeInstance instance);
}
