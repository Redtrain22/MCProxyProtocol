package me.redtrain.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;

import me.redtrain.MCProxyProtocol;

public class MCProxyProtocolFabric implements ModInitializer {
	public static final String MOD_ID = MCProxyProtocol.MOD_ID;
	public static final Logger LOGGER = MCProxyProtocol.LOGGER;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("Hello Fabric world!");

		MCProxyProtocol.init(String.format("%s/%s.toml", FabricLoader.getInstance().getConfigDir(), MOD_ID));

	}
}
