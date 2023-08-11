package wtf.ultra.hutao.command;

import net.weavemc.loader.api.command.Command;
import org.jetbrains.annotations.NotNull;

import static wtf.ultra.hutao.HuTao.*;

public class toggle extends Command {
    public toggle() {
        super("toggle");
    }

    @Override
    public void handle(@NotNull String[] args) {
        setEnabled(!enabled);
    }
}