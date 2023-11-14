package wtf.ultra.hutao.command;

import net.weavemc.loader.api.command.Command;
import org.jetbrains.annotations.NotNull;

import static wtf.ultra.hutao.HuTao.enabled;
import static wtf.ultra.hutao.HuTao.setEnabled;

public class httoggle extends Command {
    public httoggle() {
        super("httoggle");
    }

    @Override
    public void handle(@NotNull String[] args) {
        setEnabled(!enabled);
    }
}