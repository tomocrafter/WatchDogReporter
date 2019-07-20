package net.tomocraft.watchdogreporter.gui

import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.tomocraft.watchdogreporter.WatchDogReporter
import java.util.*

class GuiReport internal constructor(private val username: String) : GuiScreen() {

    private lateinit var report: GuiButton

    private val hacks = HashSet<Hacks>()

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()

        report.enabled = hacks.isNotEmpty()

        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun initGui() {
        val x = this.width / 2 - 100
        var y = this.height / 2 - 120

        for ((id, hack) in Hacks.values().withIndex()) {
            val toggle = if (hacks.contains(hack)) "§aOn" else "§cOff"
            this.buttonList.add(GuiButton(id + 1, x, y, hack.friendlyName + ": " + toggle))
            y += 25
        }
        report = GuiButton(0, x, y, "§cReport")

        this.buttonList.add(report)
        this.report.enabled = false
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.id == 0 && button.enabled) {
            val sb = StringBuilder("/wdr " + this.username + " ")
            for (hack in hacks) {
                sb.append(hack.friendlyName).append(" ")
            }
            sb.setLength(sb.length - 1)
            val command = sb.toString()

            WatchDogReporter.mc.thePlayer.sendChatMessage(command)
            mc.displayGuiScreen(null)
        } else if (button.enabled) {
            val hack = Hacks.values()[button.id - 1]
            button.displayString = hack.friendlyName + ": " + if (!hacks.contains(hack)) "§aOn" else "§cOff"
            if (hacks.contains(hack)) {
                hacks.remove(hack)
            } else {
                hacks.add(hack)
            }
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return true
    }

    enum class Hacks(val friendlyName: String) {
        FLY("Fly"),
        KILL_AURA("KillAura"),
        AUTO_CLICKER("AutoClicker"),
        SPEED("Speed"),
        ANTI_KNOCK_BACK("AntiKnockback"),
        REACH("Reach"),
        DOLPHIN("Dolphin");
    }
}
