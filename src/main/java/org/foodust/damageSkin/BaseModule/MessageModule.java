package org.foodust.damageSkin.BaseModule;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.foodust.damageSkin.DamageSkin;
import org.foodust.damageSkin.Message.BaseMessage;

import java.time.Duration;

// message 모듈
// String + String 하기 귀찮아서
// 그냥 합쳤음
// 중간에 Josa 는 명사의 조사를 붙이기 위함
public class MessageModule {
    private final HangulModule hangulModule = new HangulModule();
    private final String prefix = BaseMessage.PREFIX.getMessage();
    private final Component prefixC = MiniMessage.miniMessage().deserialize(BaseMessage.PREFIX_C.getMessage());
    private final BukkitAudiences bukkitAudiences;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    public final DamageSkin plugin;

    public MessageModule(DamageSkin plugin) {
        this.plugin = plugin;
        this.bukkitAudiences = plugin.getAdventure();
    }

    public void logInfo(String arg) {
        plugin.getLog().info(prefix + arg);
    }

    public void logInfoNoPrefix(String arg) {
        plugin.getLog().info(arg);
    }

    public void sendPlayer(Player player, String... arg) {
        player.sendMessage(prefix + makeString(arg));
    }

    public void sendPlayer(CommandSender player, String... arg) {
        player.sendMessage(prefix + makeString(arg));
    }

    public void sendPlayer(Player player, String word, HangulModule.Josa josa, String... arg) {
        player.sendMessage(prefix + hangulModule.getJosa(word, josa) + makeString(arg));
    }

    public void sendPlayerC(Player player, String... arg) {
        Component deserialize = miniMessage.deserialize(ChatColor.stripColor(makeString(arg)));
        bukkitAudiences.player(player).sendMessage(prefixC.append(deserialize));
    }

    public void sendPlayerC(Player player, Component component) {
        bukkitAudiences.player(player).sendMessage(prefixC.append(component));
    }

    public void sendPlayerC(CommandSender player, String... arg) {
        bukkitAudiences.player((Player) player).sendMessage(prefixC.append(miniMessage.deserialize(ChatColor.stripColor(makeString(arg)))));
    }

    public void sendPlayerC(Player player, String word, HangulModule.Josa josa, String... arg) {
        Component deserialize = miniMessage.deserialize(hangulModule.getJosa(word, josa) + ChatColor.stripColor(makeString(arg)));
        bukkitAudiences.player(player).sendMessage(prefixC.append(deserialize));
    }

    public void sendPlayerNoPrefix(Player player, String word, HangulModule.Josa josa, String... arg) {
        player.sendMessage(hangulModule.getJosa(word, josa) + makeString(arg));
    }

    public void sendPlayerNoPrefix(CommandSender player, String... arg) {
        player.sendMessage(makeString(arg));
    }

    public void sendPlayerNoPrefix(Player player, Component component) {
        player.sendMessage("" + component);
    }

    public void sendAllPlayer(String... arg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendPlayer(player, makeString(arg));
        });
    }

    public void sendAllPlayerC(String... arg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendPlayerC(player, ChatColor.stripColor(makeString(arg)));
        });
    }

    public void sendAllPlayerNoPrefix(String... arg) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendPlayerNoPrefix(player, arg);
        });
    }

    public void sendAllPlayerTitle(String main, String sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, main, sub, fadeIn, duration, fadeOut);
        });
    }

    public void sendAllPlayerTitle(String main, Integer fadeIn, Integer duration, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, main, fadeIn, duration, fadeOut);
        });
    }

    public void sendAllPlayerTitle(String main, Component sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, main, sub, fadeIn, duration, fadeOut);
        });
    }

    public void sendAllPlayerTitle(Component main, Component sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, main, sub, fadeIn, duration, fadeOut);
        });
    }

    public void sendAllPlayerTitle(Component main, String sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            sendTitle(player, "" + main, sub, fadeIn, duration, fadeOut);
        });
    }

    public void sendTitle(Player player, String main, String sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(miniMessage.deserialize(main), miniMessage.deserialize(sub), Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void sendTitle(Player player, Component main, String sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(main, miniMessage.deserialize(sub), Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void sendTitle(Player player, String main, Component sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(miniMessage.deserialize(main), sub, Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void sendTitle(Player player, Component main, Component sub, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(main, sub, Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void sendTitle(Player player, String main, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(miniMessage.deserialize(main), miniMessage.deserialize(""), Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void sendTitle(Player player, Component main, Integer fadeIn, Integer duration, Integer fadeOut) {
        bukkitAudiences.player(player).showTitle(Title.title(main, miniMessage.deserialize(""), Title.Times.times(Duration.ofSeconds(fadeIn), Duration.ofSeconds(duration), Duration.ofSeconds(fadeOut))));
    }

    public void broadcastMessageC(String... arg) {
        Audience players = bukkitAudiences.players();
        players.sendMessage(prefixC.append(miniMessage.deserialize(ChatColor.stripColor(makeString(arg)))));
    }

    public void broadcastMessageC(Component arg) {
        Audience players = bukkitAudiences.players();
        players.sendMessage(prefixC.append(arg));
    }

    public void broadcastMessageNoPrefix(String... arg) {
        Bukkit.broadcastMessage(makeString(arg));
    }

    public void broadcastMessageNoPrefix(Component arg) {
        Audience players = bukkitAudiences.players();
        players.sendMessage(arg);
    }

    public void broadcastMessage(String... arg) {
        Bukkit.broadcastMessage(prefix + makeString(arg));
    }

    public void broadcastMessage(HangulModule.Josa josa, String... arg) {
        Bukkit.broadcastMessage(prefix + makeString(arg));
    }

    public void broadcastMessageNoPrefix(HangulModule.Josa josa, String... arg) {
        Bukkit.broadcastMessage(makeString(arg));
    }

    public void sendPlayerActionBar(Player player, String... arg) {
        bukkitAudiences.player(player).sendActionBar(miniMessage.deserialize(makeString(arg)));
    }

    public void sendPlayerActionBar(Player player, Component arg) {
        bukkitAudiences.player(player).sendActionBar(arg);
    }

    public void clearChat(Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage("");
        }
    }

    public String makeString(String... arg) {
        StringBuilder temp = new StringBuilder();
        for (String string : arg) {
            temp.append(" ").append(string);
        }
        temp.deleteCharAt(0);
        return temp.toString();
    }
}
