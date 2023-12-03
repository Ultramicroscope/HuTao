package wtf.ultra.hutao;

import wtf.ultra.hutao.command.httoggle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.command.CommandBus;

import java.util.stream.IntStream;

@SuppressWarnings("unused")
public class HuTao implements ModInitializer {
    private static final ResourceLocation[] images;
    private final long mspf = 100;
    private long instant = 0;
    private int frame = 0;
    public static boolean enabled = false; // the mode by default is off

    public static void setEnabled(boolean value) {
        enabled = value;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "HuTao has been " + (enabled ? "enabled" : "disabled") + "."));
    }

    @Override
    public void preInit() {

        System.out.println("[HuTao] Initializing");
        CommandBus.register(new httoggle()); // command name

        EventBus.subscribe(RenderGameOverlayEvent.Pre.class, event -> {
            if (enabled) {
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
            }
        });
    }

    static {
        images = new ResourceLocation[27];
        IntStream.range(0, 27).forEach(i -> images[i] = new ResourceLocation(String.format("hutao/dance/dance%d.png", i + 1)));
    }

    public static void main(String[] args) {
        ModInitializer mod = new HuTao();
        mod.preInit();
    }
}