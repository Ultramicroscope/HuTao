package wtf.ultra.hutao;

import wtf.ultra.hutao.command.httoggle;

import org.lwjgl.input.Keyboard;

import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import net.weavemc.loader.api.command.CommandBus;

public class HuTao implements ModInitializer {
    private static final String VARIANT_DIRECTORY = ".weave/hutao";
    private static final Map<String, ResourceLocation[]> variantImages = new HashMap<>();
    private int frame = 0;
    public String currentVariant = "MaiSakurajima";
    private boolean switchVariantKeyPressed = false;
    public static boolean enabled = false;

    public static void setEnabled(boolean value) {
        enabled = value;
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(EnumChatFormatting.GRAY + "Better-HuTao has been " + (enabled ? "enabled" : "disabled") + "."));
    }

    @Override
    public void preInit() {
        System.out.println("[HuTao] Initializing");
        CommandBus.register(new httoggle());

        String homeDirectory = System.getProperty("user.home");
        Path externalDirectoryPath = Paths.get(homeDirectory, VARIANT_DIRECTORY, "dance", "custom");

        try {
            Files.createDirectories(externalDirectoryPath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        loadVariantImages(currentVariant);

        // Start a new thread to watch for image file changes
        Thread watchThread = new Thread(() -> {
            try {
                WatchService watchService = FileSystems.getDefault().newWatchService();
                externalDirectoryPath.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path changedFilePath = (Path) event.context();
                        if (changedFilePath.getFileName().toString().endsWith(".png")) {
                            System.out.println("Image file changed: " + changedFilePath);
                            loadVariantImages(currentVariant);
                        }
                    }
                    key.reset();
                    Thread.sleep(20000); // Sleep for 20 seconds
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        watchThread.start();

        EventBus.subscribe(RenderGameOverlayEvent.Pre.class, event -> {
            if (enabled) {
                Minecraft minecraft = Minecraft.getMinecraft();
                minecraft.getTextureManager().bindTexture(getCurrentImage());
                ScaledResolution resolution = new ScaledResolution(minecraft);
                int w = 256, h = 256, u = 0, v = 0;
                int x = resolution.getScaledWidth() - w;
                int y = resolution.getScaledHeight() - h;
                minecraft.ingameGUI.drawTexturedModalRect(x, y, u, v, w, h);

                if (Keyboard.isKeyDown(Keyboard.KEY_O)) {
                    if (!switchVariantKeyPressed) {
                        switchVariant();
                    }
                    switchVariantKeyPressed = true;
                } else {
                    switchVariantKeyPressed = false;
                }

                frame = (frame + 1) % (getCurrentImages().length * 2);
            }
        });
    }

    public static void main(String[] args) {
        ModInitializer mod = new HuTao();
        mod.preInit();
    }

    private void loadVariantImages(String variant) {
        variantImages.put("miku", loadVariantImagesFor(variant, 36));
        variantImages.put("aqua", loadVariantImagesFor(variant, 17));
        variantImages.put("hutao", loadVariantImagesFor(variant, 27));
        variantImages.put("MaiSakurajima", loadVariantImagesFor(variant, 16));
    }

    private ResourceLocation[] loadVariantImagesFor(String variant, int count) {
        String filePathFormat;
        if (variant.equals("custom")) {
            filePathFormat = "";
        } else {
            filePathFormat = "hutao/dance/" + variant + "/dance%d.png";
        }

        ResourceLocation[] images = new ResourceLocation[count];

        IntStream.range(0, count).forEach(i ->
                images[i] = new ResourceLocation(String.format(filePathFormat, i + 1))
        );

        return images;
    }

    private void switchVariant() {
        if (currentVariant.equals("miku")) {
            setVariant("aqua");
        } else if (currentVariant.equals("aqua")) {
            setVariant("hutao");
        } else if (currentVariant.equals("hutao")) {
            setVariant("MaiSakurajima");
        } else if (currentVariant.equals("MaiSakurajima")) {
            setVariant("miku");
        }
    }

    public void setVariant(String variant) {
        currentVariant = variant;
        loadVariantImages(currentVariant);
    }

    private ResourceLocation[] getCurrentImages() {
        return variantImages.get(currentVariant);
    }

    private ResourceLocation getCurrentImage() {
        return getCurrentImages()[frame / 2];
    }
}
