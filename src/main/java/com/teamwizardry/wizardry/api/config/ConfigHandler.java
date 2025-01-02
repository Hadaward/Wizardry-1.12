package com.teamwizardry.wizardry.api.config;

import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.config.client.ClientCategory;
import com.teamwizardry.wizardry.api.config.server.ServerCategory;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = Wizardry.MODID)
@Mod.EventBusSubscriber
public class ConfigHandler {
    public static ClientCategory client = new ClientCategory();
    public static ServerCategory server = new ServerCategory();

    @SubscribeEvent
    public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(Wizardry.MODID)) {
            ConfigManager.sync(Wizardry.MODID, Config.Type.INSTANCE);

            // TODO: Sync server settings to players
        }
    }
}
