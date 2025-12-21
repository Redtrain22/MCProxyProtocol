package me.redtrain.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import me.redtrain.MCProxyProtocol;

import net.minecraft.server.network.ServerConnectionListener;

@Mixin(ServerConnectionListener.class)
abstract class ServerConnectionListenerMixin {

  @Inject(method = "startTcpServerListener", at = @At("HEAD"))
  private void logOnStartTCPServerListener(CallbackInfo callbackInfo) {
    MCProxyProtocol.LOGGER.info(MCProxyProtocol.getText());
  }
}
