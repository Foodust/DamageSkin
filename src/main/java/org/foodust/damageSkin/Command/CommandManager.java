package org.foodust.damageSkin.Command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.foodust.damageSkin.DamageSkin;

// 커맨드 를 할 수 있게 해줍니다!
public class CommandManager implements CommandExecutor {

//    private final MessageModule messageModule;
//    private final CommandModule commandModule;

    public CommandManager(DamageSkin plugin) {
//        this.messageModule = new MessageModule(plugin);
//        this.commandModule = new CommandModule(plugin);
//        Objects.requireNonNull(plugin.getCommand(BaseMessage.COMMAND_MC_FIGHTER.getMessage())).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] data) {
        return true;
    }
}
