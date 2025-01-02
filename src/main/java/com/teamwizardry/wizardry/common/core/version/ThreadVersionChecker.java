package com.teamwizardry.wizardry.common.core.version;

import com.teamwizardry.librarianlib.core.LibrarianLib;
import com.teamwizardry.wizardry.Wizardry;
import com.teamwizardry.wizardry.api.config.ConfigHandler;
import net.minecraftforge.common.MinecraftForge;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import static com.teamwizardry.wizardry.common.core.version.VersionChecker.onlineVersion;

public class ThreadVersionChecker extends Thread {

	public ThreadVersionChecker() {
		setName("Wizardry Version Checker Thread");
		setDaemon(true);
		if (ConfigHandler.server.general.versionCheckerEnabled)
			start();
	}

	@Override
	public void run() {
		Wizardry.LOGGER.info("Checking for new updates...");
		try {
			BufferedReader r;
			URL url;
			if (LibrarianLib.DEV_ENVIRONMENT) {
				url = new URL("https://raw.githubusercontent.com/TeamWizardry/Wizardry/master/version/" + MinecraftForge.MC_VERSION + "-dev.txt");
			} else {
				url = new URL("https://raw.githubusercontent.com/TeamWizardry/Wizardry/master/version/" + MinecraftForge.MC_VERSION + ".txt");
			}
			r = new BufferedReader(new InputStreamReader(url.openStream()));

			String line;
			StringBuilder text = new StringBuilder();
			while ((line = r.readLine()) != null) {
				if (onlineVersion == null) {
					onlineVersion = line;
					text.append("VERSION: ").append(onlineVersion).append("\n");
				} else {
					if (!line.isEmpty()) {
						if (line.matches("[0-9.]*")) {
							text.append("\n").append("VERSION: ").append(line).append("\n");
						} else {
							text.append(" - ").append(line).append("\n");
						}
					}
				}
			}

			VersionChecker.updateMessage = text.toString();
			r.close();
			Wizardry.LOGGER.error("New version found! -> " + onlineVersion);
			Wizardry.LOGGER.error("Message: " + VersionChecker.updateMessage);
		} catch (Exception e) {
			Wizardry.LOGGER.error("Failed to check for updates! :(");
		}
		VersionChecker.doneChecking = true;
	}
}
