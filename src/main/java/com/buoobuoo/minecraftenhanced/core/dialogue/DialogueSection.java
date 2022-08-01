package com.buoobuoo.minecraftenhanced.core.dialogue;

import com.buoobuoo.minecraftenhanced.core.util.JSONUtil;
import com.buoobuoo.minecraftenhanced.core.util.Util;
import com.buoobuoo.minecraftenhanced.core.util.unicode.CharRepo;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

public class DialogueSection extends Section {

    private final CharRepo icon;
    private final String[] content;

    public DialogueSection(CharRepo icon, String... content) {
        this.icon = icon;
        this.content = content;
    }

    @Override
    public void execute(DialogueInstance instance) {
        Player player = instance.getPlayer();
        TextComponent[] components = new TextComponent[6];
        String[] cont = Util.padEmpty(content, 6);
        components[0] = new TextComponent(Util.formatColour(cont[0]));
        components[1] = new TextComponent(Util.formatColour(cont[1]));
        components[2] = new TextComponent(Util.formatColour(cont[2]));
        components[3] = new TextComponent(Util.formatColour(cont[3]));
        components[4] = new TextComponent(Util.formatColour(cont[4]));

        if(!instance.getTrack().isLast(this))
            components[5] = JSONUtil.getJSON(Util.formatColour("&a&l> Next"), "diagnext", false, "");
        else
            components[5] = new TextComponent(Util.formatColour(cont[5]));

        Util.sendDialogueBox(player, icon, components);
    }
}
