package wtf.ultra.hutao;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.ResourceLocation;
import net.weavemc.loader.api.ModInitializer;
import net.weavemc.loader.api.event.EventBus;
import net.weavemc.loader.api.event.RenderGameOverlayEvent;
import org.lwjgl.input.Keyboard;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class HuTao implements ModInitializer {
    private static final String VARIANT_DIRECTORY = ".weave/hutao";
    private static final Map<String, ResourceLocation[]> variantImages = new HashMap<>();
    private int frame = 0;
    private String currentVariant = "MaiSakurajima";
    private boolean switchVariantKeyPressed = false;
    private boolean modActive = true;

    @Override
    public void preInit() {

        String homeDirectory = System.getProperty("user.home");
        Path externalDirectoryPath = Paths.get(homeDirectory, VARIANT_DIRECTORY, "dance", "custom");

        try {
            Files.createDirectories(externalDirectoryPath);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        loadVariantImages(currentVariant);

        EventBus.subscribe(RenderGameOverlayEvent.Pre.class, event -> {
            if (modActive) {
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

            if (Keyboard.isKeyDown(Keyboard.KEY_I)) {
                if (!switchVariantKeyPressed) {
                    modActive = !modActive;
                }
                switchVariantKeyPressed = true;
            } else {
                switchVariantKeyPressed = false;
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
            filePathFormat = ".weave/hutao/dance/custom/dance%d.png";
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

    private void setVariant(String variant) {
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
