package me.redtrain;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.Channel;
import me.redtrain.config.MCProxyProtocolConfig;

public class MCProxyProtocol {
  public static final String MOD_ID = "mcproxyprotocol";
  public static final Logger LOGGER = LoggerFactory.getLogger(MCProxyProtocol.MOD_ID);
  public static MCProxyProtocolConfig config;

  public interface IChannelInitializer {
    void invokeInitChannel(Channel ch) throws Exception;
  }

  public static void init(File configFile) {
    config = new MCProxyProtocolConfig(configFile);
  }
}
