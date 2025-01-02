package com.teamwizardry.wizardry.api.config.server;

import net.minecraftforge.common.config.Config;

public class ItemCategory {
    @Config.Comment({
            "The buffer size a crude halo will give to a player."
    })
    public double crudeHaloBufferSize = 1000;

    @Config.Comment({
            "The buffer size a real halo will give to a player."
    })
    public double realHaloBufferSize = 5000;

    @Config.Comment({
            "The buffer size a creative halo will give to a player."
    })
    public double creativeHaloBufferSize = 50000;

    @Config.Comment({
            "Halo mana regeneration and burnout degeneration per tick."
    })
    public double haloGenSpeed = 0.001;

    @Config.Comment({
            "Pearl belt inventory size."
    })
    @Config.RangeInt(min = 1, max = 20)
    public int pearlBeltInvSize = 8;
}
