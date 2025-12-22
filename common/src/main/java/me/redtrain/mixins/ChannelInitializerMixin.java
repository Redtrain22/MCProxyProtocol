package me.redtrain.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;

@Mixin(ChannelInitializer.class)
public interface ChannelInitializerMixin {
  @Invoker(value = "initChannel", remap = false)
  void MCProxyProtocol_invokeInitChannel(Channel childHandler) throws Exception;
}
