package net.tomocraft.watchdogreporter.gui

import com.google.common.collect.ComparisonChain
import com.google.common.collect.Ordering
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.world.WorldSettings
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.input.Keyboard
import java.io.IOException
import java.util.*

class GuiWatchDogReporter : GuiScreen() {
    var currentList: MutableList<NetworkPlayerInfo> = ArrayList()
    private lateinit var selector: GuiSelector

    private var initialized: Boolean = false

    companion object {
        private val field_175252_a = Ordering.from(PlayerComparator())
        lateinit var report: GuiButton

        internal fun getUserList(par1: GuiWatchDogReporter): List<NetworkPlayerInfo> {
            return par1.currentList
        }
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        this.selector.drawScreen(mouseX, mouseY, partialTicks)
        drawCenteredString(this.fontRendererObj, "WatchDogReporter", this.width / 2, 20, 16777215)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    override fun onGuiClosed() {
        Keyboard.enableRepeatEvents(false)
    }

    @Throws(IOException::class)
    override fun handleMouseInput() {
        super.handleMouseInput()
        this.selector.handleMouseInput()
    }

    override fun initGui() {
        Keyboard.enableRepeatEvents(false)
        Keyboard.enableRepeatEvents(true)
        this.buttonList.clear()

        if (!this.initialized) {
            this.initialized = true

            this.selector = GuiSelector(this)

            this.currentList.clear()
            currentList = field_175252_a.sortedCopy(this.mc.netHandler.playerInfoMap)
        } else {
            this.selector.setDimensions(this.width, this.height, 32, this.height - 64)
        }

        report = GuiButton(0, this.width / 2 - 74, this.height - 50, 70, 20, "Report")
        this.buttonList.add(report)
        this.buttonList.add(GuiButton(1, this.width / 2 + 4, this.height - 50, 70, 20, "Reload"))
        this.selector.selectedUsernameIndex = -1
        report.enabled = false
    }

    override fun actionPerformed(button: GuiButton) {
        if (button.enabled) {
            when (button.id) {
                0 -> this.mc.displayGuiScreen(GuiReport(this.currentList[this.selector.selectedUsernameIndex].gameProfile.name))

                1 -> this.mc.displayGuiScreen(GuiWatchDogReporter())

                else -> this.selector.actionPerformed(button)
            }
        }
    }

    override fun doesGuiPauseGame(): Boolean {
        return true
    }

    @SideOnly(Side.CLIENT)
    internal class PlayerComparator : Comparator<NetworkPlayerInfo> {
        override fun compare(p_compare_1_: NetworkPlayerInfo, p_compare_2_: NetworkPlayerInfo): Int {
            val scoreplayerteam = p_compare_1_.playerTeam
            val scoreplayerteam1 = p_compare_2_.playerTeam
            return ComparisonChain.start().compareTrueFirst(p_compare_1_.gameType != WorldSettings.GameType.SPECTATOR, p_compare_2_.gameType != WorldSettings.GameType.SPECTATOR).compare(if (scoreplayerteam != null) scoreplayerteam.registeredName else "", if (scoreplayerteam1 != null) scoreplayerteam1.registeredName else "").compare(p_compare_1_.gameProfile.name, p_compare_2_.gameProfile.name).result()
        }
    }
}