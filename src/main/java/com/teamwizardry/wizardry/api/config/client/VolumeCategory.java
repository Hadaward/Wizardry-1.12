package com.teamwizardry.wizardry.api.config.client;

import net.minecraftforge.common.config.Config;

public class VolumeCategory {
    public float get(VolumeType type, float max) {
        int volume;

        switch (type) {
            case Block:
                volume = blocks;
                break;
            case Entity:
                volume = entities;
                break;
            case Item:
                volume = items;
                break;
            case Recipe:
                volume = recipes;
                break;
            case Spell:
                volume = spells;
                break;
            case UserInterface:
                volume = userInterface;
                break;
            default:
                volume = 0;
                break;
        }

        return ((float) volume / 100) * max;
    }

    @Config.Comment({
            "Volume used to play block sound effects. [0%~100%]"
    })
    @Config.Name("Blocks")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int blocks = 100;

    @Config.Comment({
            "Volume used to play entity sound effects. [0%~100%]"
    })
    @Config.Name("Entities")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int entities = 100;

    @Config.Comment({
            "Volume used to play item sound effects. [0%~100%]"
    })
    @Config.Name("Items")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int items = 100;

    @Config.Comment({
            "Volume used to play recipe sound effects. [0%~100%]"
    })
    @Config.Name("Recipes")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int recipes = 100;

    @Config.Comment({
            "Volume used to play spell sound effects. [0%~100%]"
    })
    @Config.Name("Spells")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int spells = 100;

    @Config.Comment({
            "Volume used to play gui sound effects. [0%~100%]"
    })
    @Config.Name("User Interface")
    @Config.RangeInt(min = 0, max = 100)
    @Config.SlidingOption
    public int userInterface = 100;
}
