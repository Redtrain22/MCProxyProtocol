package me.redtrain.neoforge;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLPaths;
import me.redtrain.MCProxyProtocol;

import org.slf4j.Logger;

@Mod(MCProxyProtocol.MOD_ID)
public final class MCProxyProtocolNeoForge {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = MCProxyProtocol.LOGGER;

	public MCProxyProtocolNeoForge() {
		MCProxyProtocol.init(FMLPaths.CONFIGDIR.get().resolve(MCProxyProtocol.MOD_ID.concat(".toml")).toFile());
	}
}
