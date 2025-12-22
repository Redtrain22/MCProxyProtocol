package me.redtrain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import me.redtrain.config.MCProxyProtocolConfig;

public class MCProxyProtocol {
  public static final String MOD_ID = "mcproxyprotocol";
  public static final Logger LOGGER = LoggerFactory.getLogger(MCProxyProtocol.MOD_ID);
  public static MCProxyProtocolConfig config;

  public static void init(String configFile) {
    config = new MCProxyProtocolConfig(configFile);
  }
}
