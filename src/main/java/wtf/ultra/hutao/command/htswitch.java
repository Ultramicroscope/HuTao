package wtf.ultra.hutao.command;

import net.weavemc.loader.api.command.Command;
import org.jetbrains.annotations.NotNull;

import static wtf.ultra.hutao.HuTao.setSwitchVariant;
import static wtf.ultra.hutao.HuTao.switchVariant;

public class htswitch extends Command {
    public htswitch() {
        super("htswitch");
    }

    @Override
    public void handle(@NotNull String[] args) {
        setSwitchVariant(!switchVariant);
    }
}