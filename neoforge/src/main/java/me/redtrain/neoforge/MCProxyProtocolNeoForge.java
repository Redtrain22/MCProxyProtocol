package me.redtrain.neoforge;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import me.redtrain.MCProxyProtocol;

import org.slf4j.Logger;

@Mod(MCProxyProtocol.MOD_ID)
public final class MCProxyProtocolNeoForge {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = MCProxyProtocol.LOGGER;

	public MCProxyProtocolNeoForge(ModContainer container) {
		LOGGER.info("Hello Neoforge World!");

	}

	// @Override
	// public void onInitialize() {
	// // This code runs as soon as Minecraft is in a mod-load-ready state.
	// // However, some things (like resources) may still be uninitialized.
	// // Proceed with mild caution.

	// LOGGER.info("Hello Fabric world!");
	// }
}
