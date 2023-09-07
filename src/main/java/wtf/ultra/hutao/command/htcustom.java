package wtf.ultra.hutao.command;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.weavemc.loader.api.command.Command;
import org.jetbrains.annotations.NotNull;

import static wtf.ultra.hutao.HuTao.customImage;


public class htcustom extends Command {
    public htcustom() {
        super("htcustom");
    }

    @Override
    public void handle(@NotNull String[] args) {
        if (args.length == 1) {
            try {
                float secondFactor = Float.parseFloat(args[0]);
                customImage((int) secondFactor);
            } catch (NumberFormatException e) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Invalid factor value. Usage: /htcustom <value of images>"));
            }
        } else {
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Usage: /htcustom <amout of images>"));
        }
    }
}