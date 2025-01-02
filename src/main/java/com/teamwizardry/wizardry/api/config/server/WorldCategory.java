package com.teamwizardry.wizardry.api.config.server;

import net.minecraftforge.common.config.Config;

public class WorldCategory {
    @Config.Comment({
            "If true, mana pool dimension whitelist is instead a blacklist."
    })
    public boolean isDimBlacklist = false;

    @Config.Comment({
            "Whitelisted dimensions for mana pool generation."
    })
    public int[] manaPoolDimWhitelist = {0};

    @Config.Comment({
            "How rare the mana pool is in terms of 1 in X. Set to 0 to disable generation."
    })
    @Config.RangeInt(min = 0)
    public int manaPoolRarity = 25;

    @Config.Comment({
            "If you have a dimension ID conflict with this mod and something else, change this number."
    })
    public int underworldID = 33;

    @Config.Comment({
            "If you have a dimension ID conflict with this mod and something else, change this number."
    })
    public int torikkiID = 34;

    @Config.Comment({
            "Minimum fall speed required to have to smack a block into to teleport to the underworld (in blocks). Positive values disable teleporting."
    })
    public double underworldFallSpeed = -2.7;

    @Config.Comment({
            "The maximum possible distance required for 2 mana interacting blocks to link to each other."
    })
    public int networkLinkDistance = 32;
}
