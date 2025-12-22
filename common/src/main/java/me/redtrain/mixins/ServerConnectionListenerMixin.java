package me.redtrain.mixins;

import java.net.InetSocketAddress;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import inet.ipaddr.IPAddressString;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.haproxy.HAProxyMessageDecoder;
import me.redtrain.MCProxyProtocol;
import me.redtrain.MCProxyProtocolHandler;
import net.minecraft.server.network.ServerConnectionListener;

@Mixin(ServerConnectionListener.class)
abstract class ServerConnectionListenerMixin {

  @Redirect(method = "startTcpServerListener", at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/ServerBootstrap;childHandler(Lio/netty/channel/ChannelHandler;)Lio/netty/bootstrap/ServerBootstrap;"), remap = false)
  private ServerBootstrap MCProxyProtocol_logOnStartTCPServerListener(ServerBootstrap bootstrap,
      ChannelHandler childHandler) {

    return bootstrap.childHandler(new ChannelInitializer<Channel>() {
      @Override
      protected void initChannel(Channel channel) throws Exception {
        final ChannelInitializerMixin initializer = ((ChannelInitializerMixin) childHandler);
        initializer.MCProxyProtocol_invokeInitChannel(channel);

        if (!MCProxyProtocol.config.config.enabled)
          return;

        final InetSocketAddress socketAddress = (InetSocketAddress) channel.remoteAddress();
        final IPAddressString remoteAddress = new IPAddressString(socketAddress.getHostString());

        // All trusted CIDRs will have the ProxyProtocol Handler set
        for (String trustedCIDR : MCProxyProtocol.config.config.trustedAddresses) {
          if (new IPAddressString(trustedCIDR).contains(remoteAddress)) {
            channel.pipeline()
                .addAfter("timeout", "haproxy-decoder", new HAProxyMessageDecoder())
                .addAfter("haproxy-decoder", "haproxy-handler", new MCProxyProtocolHandler());

            MCProxyProtocol.LOGGER
                .debug("Applying ProxyProtocol Handler to trusted proxy and accepting connection from: "
                    + remoteAddress.toString());
            return;
          }
        }

        // All CIDRs in this list will not have the ProxyProtocol Handler registered.
        // This means that when the remote connects it will be direct and any packets
        // with ProxyProtocol
        // WILL result in the inability to connect
        for (String bypassCIDR : MCProxyProtocol.config.config.bypassAddresses) {
          if (new IPAddressString(bypassCIDR).contains(remoteAddress)) {
            MCProxyProtocol.LOGGER.debug("Allowing direct connect from client at: " + remoteAddress.toString());

            return;
          }
        }

        MCProxyProtocol.LOGGER.debug("Client not in Trusted Address list or Bypass Address list. Rejecting client: "
            + remoteAddress.toString());

        // Close the stream for any other request they shouldn't be allowed.
        // Remote Address isn't in bypass address list OR trusted address list.
        channel.close();
        return;
      }
    });
  }

}
