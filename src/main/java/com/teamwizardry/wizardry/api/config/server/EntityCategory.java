package com.teamwizardry.wizardry.api.config.server;

import net.minecraftforge.common.config.Config;

public class EntityCategory {
    @Config.Comment({
            "Defines the reach distance of fairies in terms of blocks.",
    })
    @Config.RangeDouble(min = 1, max = 16)
    public double fairyReach = 3;

    @Config.Comment({
            "The maximum number of zombies a player can have active at once via the reinforcements spell.",
    })
    @Config.RangeInt(min = 1, max = 1000)
    public int maxZombies = 10;
}
