package net.tomocraft.watchdogreporter

import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent
import net.tomocraft.watchdogreporter.gui.GuiWatchDogReporter
import org.lwjgl.input.Keyboard

@Mod(modid = "watchdogreporter", version = "1.0.0", clientSideOnly = true, acceptedMinecraftVersions = "[1.8.9]")
class WatchDogReporter {
    companion object {
        val mc = Minecraft.getMinecraft()!!
    }

    private var onHypixel: Boolean = false
    private lateinit var hotKey: KeyBinding

    @Mod.EventHandler
    fun onInit(e: FMLInitializationEvent) {
        mc.gameSettings.loadOptions()

        hotKey = KeyBinding("Report The Cheater", Keyboard.KEY_H, "WatchDogReporter")
        ClientRegistry.registerKeyBinding(hotKey)

        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onLoggedIn(e: FMLNetworkEvent.ClientConnectedToServerEvent) {
        this.onHypixel = !WatchDogReporter.mc.isConnectedToRealms && e.manager.remoteAddress.toString().toLowerCase().contains("hypixel.net")
    }

    @SubscribeEvent
    fun onLoggedOut(e: FMLNetworkEvent.ClientDisconnectionFromServerEvent) {
        this.onHypixel = false
    }

    @SubscribeEvent
    fun onKeyInput(e: InputEvent.KeyInputEvent) {
        if (this.hotKey.isPressed && this.onHypixel) {
            WatchDogReporter.mc.displayGuiScreen(GuiWatchDogReporter())
        }
    }
}
