package com.teamwizardry.wizardry.api.config.server;

import net.minecraftforge.common.config.Config;

public class SpellCategory {
    @Config.Comment({
            "The multiplier a spellData gets for a perfect or ancient quality output. [1,2]",
            "This will be multiplied by the quality value of the output, which is 1.0 for apex pearls and greater for ancient pearls."
    })
    @Config.RangeDouble(min = 1, max = 2)
    public double perfectPearlMultiplier = 1.2;

    @Config.Comment({
            "The multiplier a spellData gets, as a flat rate, for a depleted quality output. [0.001,0.1]"
    })
    @Config.RangeDouble(min = 0.001, max = 0.1)
    public double damagedPearlMultiplier = 0.05;

    @Config.Comment({
            "Maximum number of ticks between Zone activations. Minimum of 1."
    })
    @Config.RangeInt(min = 1)
    public int zoneTimer = 20;

    @Config.Comment({
            "Maximum number of ticks between Beam activations. Minimum of 1."
    })
    @Config.RangeInt(min = 1)
    public int beamTimer = 10;
}
