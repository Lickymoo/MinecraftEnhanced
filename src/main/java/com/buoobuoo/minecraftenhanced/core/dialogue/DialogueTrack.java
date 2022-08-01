package com.buoobuoo.minecraftenhanced.core.dialogue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogueTrack {
    private List<Section> sections = new ArrayList<>();

    public Section getSection(int sect){
        return sections.get(sect);
    }

    public void addSection(Section... sects){
        sections.addAll(Arrays.asList(sects));
    }

    public int getSize(){
        return sections.size();
    }

    public boolean isLast(Section sect){
        return (sections.get(sections.size()-1) == sect);
    }
}
