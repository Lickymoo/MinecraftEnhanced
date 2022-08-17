package com.buoobuoo.minecraftenhanced.core.entity.interf;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public interface ModelEntity extends CustomEntity {

    Map<ModelEntity, ActiveModel> activeModelMap = new HashMap<>();
    Map<ModelEntity, ModeledEntity> modeledEntityMap = new HashMap<>();
    String modelName();

    @Override
    default void spawnEntity(MinecraftEnhanced plugin, Location loc){
        CustomEntity.super.spawnEntity(plugin, loc);

        org.bukkit.entity.Entity bukkitEntity = asEntity().getBukkitEntity();

        ActiveModel model = ModelEngineAPI.api.getModelManager().createActiveModel(modelName());
        ModeledEntity modeledEntity = ModelEngineAPI.api.getModelManager().createModeledEntity(bukkitEntity);

        modeledEntity.addActiveModel(model);
        modeledEntity.detectPlayers();
        modeledEntity.setInvisible(true);

        activeModelMap.put(this, model);
        modeledEntityMap.put(this, modeledEntity);
    }

    @Override
    default void destroyEntity(){
        activeModelMap.remove(this);
        modeledEntityMap.remove(this);
        CustomEntity.super.destroyEntity();
    }

    @Override
    default void hideEntity(Player player){

        CustomEntity.super.hideEntity(player);
        ActiveModel modelEntity = activeModelMap.get(this);
        if(modelEntity == null)
            return;

        modelEntity.hideModel(player);
    }


    @Override
    default void showEntity(Player player){
        if(!canShow(player))
            return;
        CustomEntity.super.showEntity(player);
        ActiveModel modelEntity = activeModelMap.get(this);
        if(modelEntity == null)
            return;

        modelEntity.showModel(player);
    }
}






































