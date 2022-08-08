package com.buoobuoo.minecraftenhanced.core.entity.interf;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.ticxo.modelengine.api.ModelEngineAPI;
import com.ticxo.modelengine.api.model.ActiveModel;
import com.ticxo.modelengine.api.model.ModeledEntity;
import org.bukkit.Location;

public interface ModelEntity extends CustomEntity {

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
    }
}
