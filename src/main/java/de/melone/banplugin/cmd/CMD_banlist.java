package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class CMD_banlist implements SimpleCommand {

    private final ProxyServer proxy;
    private static Player player;

    public CMD_banlist(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player)) {
            System.out.println("This Command is Only for Player");
            return;
        }

        Player player = (Player) source;

        if (!player.hasPermission("Ban.ban")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + BanPlugin.noperms));
            return;
        }

        player.sendMessage(MiniMessage.miniMessage().deserialize("========== " + BanPlugin.prefixMiniMessage + " ==========" +
                "<newline>1. " + BanPlugin.reson1 +
                "<newline>2. " + BanPlugin.reson2 +
                "<newline>3. " + BanPlugin.reson3 +
                "<newline>4. " + BanPlugin.reson4 +
                "<newline>5. " + BanPlugin.reson5 +
                "<newline>6. " + BanPlugin.reson6 +
                "<newline>7. " + BanPlugin.reson7 +
                "<newline>8. " + BanPlugin.reson8 +
                "<newline>9. " + BanPlugin.reson9 +
                "<newline>10. " + BanPlugin.reson10 +
                "<newline>11. " + BanPlugin.reson11 +
                "<newline>12. " + BanPlugin.reson12
        ));
    }
}
