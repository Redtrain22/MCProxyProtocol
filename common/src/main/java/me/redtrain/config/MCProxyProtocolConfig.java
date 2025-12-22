package me.redtrain.config;

import java.util.ArrayList;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigSpec;
import com.electronwill.nightconfig.core.ConfigSpec.CorrectionListener;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.serde.ObjectDeserializer;
import com.electronwill.nightconfig.core.serde.ObjectSerializer;
import com.electronwill.nightconfig.core.serde.annotations.SerdeComment;

import inet.ipaddr.IPAddressString;
import me.redtrain.MCProxyProtocol;

public class MCProxyProtocolConfig {
  public ConfigData config;
  ConfigSpec spec;

  public class ConfigData {
    @SerdeComment("Whether to enable or disable the mod")
    public boolean enabled;

    @SerdeComment("List of addresses to trust to provide the PROXY header from")
    @SerdeComment("All connections from these IPs MUST set the PROXY header")
    public ArrayList<String> trustedAddresses;

    @SerdeComment("List of addresses that bypass the handling of ProxyProtocol")
    @SerdeComment("This means that these addresses WILL NOT be sending ProxyProtocol")
    public ArrayList<String> bypassAddresses;

    public ConfigData() {
      this.enabled = true;
      this.trustedAddresses = new ArrayList<String>();
      this.bypassAddresses = new ArrayList<String>();

      this.trustedAddresses.add("10.0.0.0/24");
      this.trustedAddresses.add("192.168.0.0/24");

      this.bypassAddresses.add("127.0.0.1/8");
      this.bypassAddresses.add("fe80::/10");
    }
  }

  private void createSpec() {
    this.spec = new ConfigSpec();

    ConfigData defaultConfig = new ConfigData();

    parseAddresses(defaultConfig.trustedAddresses);

    spec.define("enabled", defaultConfig.enabled);
    spec.define("trustedAddresses", defaultConfig.trustedAddresses);
    spec.define("bypassAddresses", defaultConfig.bypassAddresses);
  }

  public MCProxyProtocolConfig(String configFile) {
    createSpec();

    CommentedFileConfig config = CommentedFileConfig.of(configFile);

    config.load();

    if (config.isEmpty()) {
      MCProxyProtocol.LOGGER.info("Detected empty config file, creating one with the defaults");

      config.addAll(ObjectSerializer.standard().serializeFields(new ConfigData(), CommentedConfig::inMemory));
      config.save();
    }

    if (!spec.isCorrect(config)) {
      CorrectionListener listener = (action, path, incorrectValue, correctValue) -> {
        String pathString = String.join(",", path);
        MCProxyProtocol.LOGGER
            .warn(String.format("Correcting %s: The value supplied, %s, is incorrect. Correcting to %s", pathString,
                incorrectValue, correctValue));
      };

      spec.correct(config, listener);

      // Must save after correcting. Correcting does not trigger a save.
      config.save();
    }

    // The config should be valid by this point. Deserialize it the config field.
    this.config = ObjectDeserializer.standard().deserializeFields(config, ConfigData::new);

    // Close the config as we should no longer need it.
    config.close();
  }

  public void parseAddresses(ArrayList<String> addresses) {
    for (String address : addresses) {
      MCProxyProtocol.LOGGER.info((new IPAddressString(address)).toString());
    }
  }
}
