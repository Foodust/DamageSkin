package org.foodust.damageSkin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.message.BaseMessage;

import java.util.Objects;

public class CommandManager implements CommandExecutor {

    private final DamageSkin plugin;

    public CommandManager(DamageSkin plugin) {
        this.plugin = plugin;
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_DAMAGE_SKIN.getMessage())).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_DAMAGE_SKIN.getMessage())).setTabCompleter(new CommandSub(plugin));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] data) {
        if (!sender.isOp()) return false;
        if (data.length == 0) {
            return true;
        }

        BaseMessage byBaseMessage = BaseMessage.getByMessage(data[0]);
        switch (byBaseMessage) {
            case COMMAND_SET -> plugin.getSkinModule().commandSet(sender, data);
            case COMMAND_SET_ALL -> plugin.getSkinModule().commandSetAll(sender, data);
            case COMMAND_REMOVE -> plugin.getSkinModule().commandRemove(sender, data);
            case COMMAND_RELOAD -> plugin.getSkinModule().commandReload(sender);
            default -> sender.sendMessage(BaseMessage.PREFIX_C.getMessage() + BaseMessage.ERROR_WRONG_COMMAND.getMessage());
        }
        return true;
    }
}
