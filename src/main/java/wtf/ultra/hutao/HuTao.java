package wtf.ultra.hutao;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class HuTao implements ModInitializer {
    private static final ResourceLocation[] images;
    private final long mspf = 100;
    private long instant = 0;
    private int frame = 0;

    @Override
    public void preInit() {
        EventBus.subscribe(RenderGameOverlayEvent.Pre.class, event -> {
            long now;
            if ((now = System.currentTimeMillis()) - instant >= mspf) {
                instant = now;
                frame = (frame + 1) % images.length;
            }

            Minecraft mc = Minecraft.getMinecraft();
            mc.getTextureManager().bindTexture(images[frame]);
            ScaledResolution resolution = new ScaledResolution(mc);
            int w = 256, h = 256, u = 0, v = 0;
            int x = resolution.getScaledWidth() - w;
            int y = resolution.getScaledHeight() - h;
            mc.ingameGUI.drawTexturedModalRect(x, y, u, v, w, h);
        });
    }

    static {
        images = new ResourceLocation[27];
        IntStream.range(0, 27).forEach(i -> images[i] = new ResourceLocation(String.format("hutao/dance/dance%d.png", i + 1)));
    }
}
