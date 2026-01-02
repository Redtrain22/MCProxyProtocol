package me.redtrain;

import java.net.InetSocketAddress;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.haproxy.HAProxyCommand;
import io.netty.handler.codec.haproxy.HAProxyMessage;
import me.redtrain.mixins.ConnectionMixin;
import net.minecraft.network.Connection;

public class MCProxyProtocolHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext context, Object message) throws Exception {

    // We only should be handling HAProxyMessage messages
    // Send all others to wherever they need to go
    if (!(message instanceof HAProxyMessage)) {
      super.channelRead(context, message);
      return;
    }

    final HAProxyMessage streamMessage = (HAProxyMessage) message;
    if (streamMessage.command() != HAProxyCommand.PROXY)
      return;

    final String realAddress = streamMessage.sourceAddress();
    final int realPort = streamMessage.sourcePort();

    if (realAddress == null) {
      MCProxyProtocol.LOGGER.debug("PROXY Header was sent, but no valid source address was found from client: "
          + context.channel().remoteAddress());
      context.close();
      return;
    }

    MCProxyProtocol.LOGGER.debug("Socket Address after PROXY Header handling: " + realAddress.toString());

    final InetSocketAddress clientAddress = new InetSocketAddress(realAddress, realPort);
    final Connection conn = (Connection) context.pipeline().get("packet_handler");

    ((ConnectionMixin) conn).setAddress(clientAddress);

    streamMessage.release();
  }
}
