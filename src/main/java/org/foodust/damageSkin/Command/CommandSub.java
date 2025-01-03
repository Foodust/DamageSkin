package org.foodust.damageSkin.Command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.foodust.damageSkin.Data.SkinData;
import org.foodust.damageSkin.Message.BaseMessage;

import java.util.*;
import java.util.stream.Collectors;

public class CommandSub implements TabCompleter {
    Set<String> mainSub = EnumSet.range(BaseMessage.COMMAND_SET, BaseMessage.COMMAND_RELOAD).stream().map(BaseMessage::getMessage).collect(Collectors.toSet());
    Set<String> skinsSub = SkinData.skins.keySet();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], mainSub, completions);
        } else if (args.length == 2) {
            Set<String> whoSub = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toSet());
            switch (BaseMessage.getByMessage(args[0])) {
                case COMMAND_SET, COMMAND_REMOVE -> StringUtil.copyPartialMatches(args[1], whoSub, completions);
            }
        } else if (args.length == 3) {
            StringUtil.copyPartialMatches(args[2], skinsSub, completions);
        }
        Collections.sort(completions);
        return completions;
    }
}
