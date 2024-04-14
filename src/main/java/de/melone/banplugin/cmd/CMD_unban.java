package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.ulti.BanSQL;

import java.util.Optional;

public class CMD_unban implements SimpleCommand {

    private final ProxyServer proxy;

    public CMD_unban(ProxyServer proxy) {
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
                if (optionalPlayer.isPresent()) {
                    Player targetPlayer = optionalPlayer.get();

                    BanSQL.Playerunban(targetPlayer, player);
                }
            }
        }
    }
}
