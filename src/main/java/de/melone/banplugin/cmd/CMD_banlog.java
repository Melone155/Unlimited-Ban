package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.LocalDateTime;
import java.util.Optional;

public class CMD_banlog implements SimpleCommand {

    private final ProxyServer proxy;

    public CMD_banlog(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            Player player = (Player) source;
            if (invocation.arguments().length == 1) {
                String playerName = args[0];

                Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                Player targetPlayer = optionalPlayer.get();
                if (optionalPlayer.isPresent()) {

                    BanlogSQL.GetPlayerBan(targetPlayer.getUniqueId().toString(), player);
                }
            } else if (invocation.arguments().length == 2) {
                String playerName = args[0];
                Integer count = Integer.valueOf(args[1]);

                Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                Player targetPlayer = optionalPlayer.get();

                BanlogSQL.GetPlayerBanCount(targetPlayer.getUniqueId().toString(), player, count);
            }
        }
    }
}
