package wtf.ultra.hutao;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.stream.IntStream;

@Mod(modid = "hutao", version = "1.0")
public class HuTao {
    private static final ResourceLocation[] images;
    private final Minecraft mc = Minecraft.getMinecraft();
    private final long mspf = 100;
    private long instant = 0;
    private int frame = 0;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void renderOverlay(RenderGameOverlayEvent.Text event) {
        long now;
        if ((now = System.currentTimeMillis()) - instant >= mspf) {
            instant = now;
            frame = (frame + 1) % images.length;
        }

        mc.fontRendererObj.drawString(EnumChatFormatting.WHITE.toString(), 0, 0, 0);
        mc.getTextureManager().bindTexture(images[frame]);
        ScaledResolution resolution = new ScaledResolution(mc);
        int w = 256, h = 256, u = 0, v = 0;
        int x = resolution.getScaledWidth() - w;
        int y = resolution.getScaledHeight() - h;
        mc.ingameGUI.drawTexturedModalRect(x, y, u, v, w, h);
    }

    static {
        images = new ResourceLocation[27];
        IntStream.range(0, 27).forEach(i -> images[i] = new ResourceLocation("hutao", String.format("dance/dance%d.png", i + 1)));
    }
}
