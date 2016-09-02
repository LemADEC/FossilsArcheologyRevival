package fossilsarcheology.client.gui;

import fossilsarcheology.server.block.entity.TileEntityAnalyzer;
import fossilsarcheology.server.container.AnalyzerContainer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AnalyzerGUI extends GuiContainer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("fossil:textures/gui/Analyser.png");

    private TileEntityAnalyzer tile;

    public AnalyzerGUI(InventoryPlayer playerInventory, TileEntity tile) {
        super(new AnalyzerContainer(playerInventory, tile));
        this.tile = (TileEntityAnalyzer) tile;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        String customName = this.tile.hasCustomInventoryName() ? this.tile.getInventoryName() : I18n.format(this.tile.getInventoryName());
        this.fontRendererObj.drawString(customName, this.xSize / 2 - this.fontRendererObj.getStringWidth(customName) / 2, 6, 4210752);
        this.fontRendererObj.drawString(I18n.format("container.inventory"), 8, this.ySize - 96 + 2, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(TEXTURE);
        int drawX = (this.width - this.xSize) / 2;
        int drawY = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(drawX, drawY, 0, 0, this.xSize, this.ySize);
        int progress = this.tile.getAnalyzeProgressScaled(22);
        this.drawTexturedModalRect(drawX + 80, drawY + 22, 177, 18, progress, 9);
    }
}
