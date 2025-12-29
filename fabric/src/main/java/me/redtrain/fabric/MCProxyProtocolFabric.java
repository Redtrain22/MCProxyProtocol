package me.redtrain.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

import org.slf4j.Logger;

import me.redtrain.MCProxyProtocol;

public class MCProxyProtocolFabric implements ModInitializer {
	public static final Logger LOGGER = MCProxyProtocol.LOGGER;

	@Override
	public void onInitialize() {
		MCProxyProtocol.init(FabricLoader.getInstance().getConfigDir().resolve(MCProxyProtocol.MOD_ID + ".toml").toFile());

	}
}
