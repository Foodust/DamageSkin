package org.foodust.damageSkin.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.message.BaseMessage;

import java.util.*;
import java.util.stream.Collectors;

public class CommandSub implements TabCompleter {

    private final DamageSkin plugin;
    private final Set<String> mainSub = EnumSet.range(BaseMessage.COMMAND_SET, BaseMessage.COMMAND_RELOAD)
            .stream()
            .map(BaseMessage::getMessage)
            .collect(Collectors.toSet());

    public CommandSub(DamageSkin plugin) {
        this.plugin = plugin;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], mainSub, completions);
        } else if (args.length == 2) {
            Set<String> whoSub = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toSet());
            switch (BaseMessage.getByMessage(args[0])) {
                case COMMAND_SET, COMMAND_REMOVE -> StringUtil.copyPartialMatches(args[1], whoSub, completions);
            }
        } else if (args.length == 3) {
            Set<String> skinsSub = plugin.getSkinModule().getSkins().keySet();
            StringUtil.copyPartialMatches(args[2], skinsSub, completions);
        }

        Collections.sort(completions);
        return completions;
    }
}
