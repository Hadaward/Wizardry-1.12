package com.teamwizardry.wizardry.api.config.server;

import net.minecraftforge.common.config.Config;

public class GeneralCategory {
    @Config.Comment({
            "If enabled, will inform you of new updates to the mod."
    })
    @Config.Name("Version Checker Enabled")
    public boolean versionCheckerEnabled = true;

    @Config.Comment({
            "If enabled, will print out detailed logging info during startup."
    })
    @Config.Name("Debug Info")
    public boolean debugInfo = false;

    @Config.Comment({
            "If enabled, external recipes and modules will be forcibly reset to default.",
            "Disable to allow custom recipes and module values."
    })
    @Config.Name("Use Internal Values")
    public boolean useInternalValues = false;
}
