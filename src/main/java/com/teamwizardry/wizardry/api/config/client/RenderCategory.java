package com.teamwizardry.wizardry.api.config.client;

import net.minecraftforge.common.config.Config;

public class RenderCategory {
    @Config.Comment({
            "If enabled, the crude halo will render."
    })
    @Config.Name("Render Crude Halo")
    public boolean renderCrudeHalo = true;

    @Config.Comment({
            "If enabled, the real halo will render."
    })
    @Config.Name("Render Real Halo")
    public boolean renderRealHalo = true;

    @Config.Comment({
            "If enabled, the creative halo will render."
    })
    @Config.Name("Render Creative Halo")
    public boolean renderCreativeHalo = true;

    //TODO: Remove once we have a real cosmetics system
    @Config.Comment({
            "If enabled, the cape will render."
    })
    @Config.Name("Render Cape")
    public boolean renderCape = true;
}
