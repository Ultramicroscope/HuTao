package wtf.ultra.hutao;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;


import java.util.stream.IntStream;

public class HuTao implements ModInitializer {
    private static final ResourceLocation[] images;
    private int frame = 0;

    @Override
    public void preInit() {
        EventBus.subscribe(RenderGameOverlayEvent.Pre.class, event -> {
            Minecraft.getMinecraft().getTextureManager().bindTexture(images[frame / 2]);
            ScaledResolution resolution = new ScaledResolution(Minecraft.getMinecraft());
            int w = 256, h = 256, u = 0, v = 0;
            int x = resolution.getScaledWidth() - w;
            int y = resolution.getScaledHeight() - h;
            Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x, y, u, v, w, h);

            frame = (frame + 1) % 54;
        });
    }

    static {
        images = new ResourceLocation[27];
        IntStream.range(0, 27).forEach(i -> images[i] = new ResourceLocation(String.format("hutao/dance/dance%d.png", i + 1)));
    }
}
