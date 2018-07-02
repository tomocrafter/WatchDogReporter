package net.tomocraft.watchdogreporter.gui

import net.minecraft.client.gui.Gui
import net.minecraft.client.gui.GuiSlot
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.scoreboard.ScorePlayerTeam
import org.lwjgl.opengl.GL11

class GuiSelector internal constructor(private val parentGui: GuiWatchDogReporter) : GuiSlot(parentGui.mc, parentGui.width, parentGui.height, 32, parentGui.height - 65 + 4, 18) {
    internal var selectedUsernameIndex: Int = 0

    private fun getPlayerName(networkPlayerInfoIn: NetworkPlayerInfo) = if (networkPlayerInfoIn.displayName != null)
            networkPlayerInfoIn.displayName.formattedText
        else
            ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.playerTeam, networkPlayerInfoIn.gameProfile.name)

    override fun getScrollBarX() = super.getScrollBarX() + 30

    override fun getListWidth() = super.getListWidth() + 85

    override fun getSize() = GuiWatchDogReporter.getUserList(this.parentGui).size

    override fun isSelected(slotIndex: Int) = slotIndex == this.selectedUsernameIndex

    override fun drawBackground() = this.parentGui.drawDefaultBackground()

    override fun drawSlot(entryID: Int, p_180791_2_: Int, p_180791_3_: Int, p_180791_4_: Int, mouseXIn: Int, mouseYIn: Int) {
        val networkPlayerInfo = GuiWatchDogReporter.getUserList(this.parentGui)[entryID]
        val name = this.getPlayerName(networkPlayerInfo)
        val gameProfile = networkPlayerInfo.gameProfile

        val entityPlayer = this.mc.theWorld.getPlayerEntityByUUID(gameProfile.id)
        val isSpecial = entityPlayer != null && entityPlayer.isWearing(EnumPlayerModelParts.CAPE) && (gameProfile.name == "Dinnerbone" || gameProfile.name == "Grumm")
        this.mc.textureManager.bindTexture(networkPlayerInfo.locationSkin)

        val l2 = 8 + if (isSpecial) 8 else 0
        val i3 = 8 * if (isSpecial) -1 else 1

        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        Gui.drawScaledCustomSizeModalRect(this.parentGui.width / 2 - 100, p_180791_3_ + 1, 8.0f, l2.toFloat(), 8, i3, 8, 8, 64.0f, 64.0f)
        this.parentGui.drawCenteredString(this.parentGui.mc.fontRendererObj, name, this.parentGui.width / 2, p_180791_3_ + 1, 0xFFFFFF)
        GL11.glPopMatrix()
    }

    override fun elementClicked(slotIndex: Int, isDoubleClick: Boolean, mouseX: Int, mouseY: Int) {
        this.selectedUsernameIndex = slotIndex
        GuiWatchDogReporter.report.enabled = this.selectedUsernameIndex in 0..(size - 1)
    }
}
