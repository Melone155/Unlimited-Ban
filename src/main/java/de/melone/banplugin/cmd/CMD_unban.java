package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanSQL;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Optional;

public class CMD_unban implements SimpleCommand {

    private final ProxyServer proxy;
    private static Player player;
    private static Player targetPlayer;

    public CMD_unban(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            player = (Player) source;
            if (invocation.arguments().length == 1) {
                String playerName = args[0];

                Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                if (optionalPlayer.isPresent()) {
                    targetPlayer = optionalPlayer.get();

                    BanSQL.Playerunban(targetPlayer, player);
                    player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.Playerunban)));
                }
            }
        }
    }

    private static String ConfigMessages(String message) {
        if (message.contains("%spieler%") || message.contains("%targetPlayer%") || message.contains("%reson%") || message.contains("%prefix%")) {
            return message.replace("%spieler%", player.getUsername())
                    .replace("%targetPlayer%", targetPlayer.getUsername())
                    .replace("%prefix%", BanPlugin.prefixMiniMessage);
        }
        return message;
    }
}
