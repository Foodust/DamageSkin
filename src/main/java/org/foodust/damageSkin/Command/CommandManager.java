package org.foodust.damageSkin.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.foodust.damageSkin.BaseModule.ConfigModule;
import org.foodust.damageSkin.BaseModule.MessageModule;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Message.BaseMessage;

import java.util.Objects;

// 커맨드 를 할 수 있게 해줍니다!
public class CommandManager implements CommandExecutor {

    private final MessageModule messageModule;
    private final ConfigModule configModule;

    public CommandManager(DamageSkin plugin) {
        this.messageModule = new MessageModule(plugin);
        this.configModule = new ConfigModule(plugin);
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_DAMAGE_SKIN.getMessage())).setExecutor(this);
        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_DAMAGE_SKIN.getMessage())).setTabCompleter(new CommandSub());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] data) {
        if (!sender.isOp()) return false;
        if (data.length == 0) {
            return true;
        } else {
            BaseMessage byBaseMessage = BaseMessage.getByMessage(data[0]);
            switch (byBaseMessage) {
                case COMMAND_SET -> configModule.commandSet(sender, data);
                case COMMAND_REMOVE -> configModule.commandRemove(sender, data);
                case COMMAND_RELOAD -> configModule.commandReload(sender, data);
                default -> messageModule.sendPlayerC(sender, BaseMessage.ERROR_WRONG_COMMAND.getMessage());
            }
        }
        return true;
    }
}
