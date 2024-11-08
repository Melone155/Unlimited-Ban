package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.Ban;
import de.melone.banplugin.ulti.Banlog;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.LocalDateTime;
import java.util.Optional;

public class CMD_tempban implements SimpleCommand{

    private final ProxyServer proxy;
    private static Player player;
    private static Player targetPlayer;
    private static String reson;


    public CMD_tempban(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(Invocation invocation) {

        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (!(source instanceof Player player)) {
            System.out.println("This Command is Only for Player");
            return;
        }

        if (!player.hasPermission("Ban.tempban")) {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + BanPlugin.noperms));
            return;
        }


        //tempban <Player> <time in h> <Type> <resion>

        if (invocation.arguments().length < 4) {
            String playerName = args[0];
            String time = args[1];
            String type = args[2];
            String reason = String.join(" ", java.util.Arrays.copyOfRange(args, 3, args.length));

            int timeint;
            timeint = Integer.parseInt(time);

            Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
            if (optionalPlayer.isPresent()) {
                targetPlayer = optionalPlayer.get();
                LocalDateTime timenow = LocalDateTime.now();
                String PlayerIPAddress = String.valueOf(targetPlayer.getRemoteAddress());

                if (type.equalsIgnoreCase("server")) {
                    BanPlayer(targetPlayer, player, PlayerIPAddress, timenow, reason, timeint);
                    return;
                }

                if (type.equalsIgnoreCase("chat")) {
                    ChatBanPlayer(targetPlayer, player, PlayerIPAddress, timenow, reason, timeint);
                    return;
                }

                player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + BanPlugin.errortempban));
            }
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + BanPlugin.tempbanhelp));
        }
    }

    private static String ConfigMessages(String message) {
        String targetName = (targetPlayer != null) ? targetPlayer.getUsername() : "Unknown";
        String playerName = (player != null) ? player.getUsername() : "Unknown";

        return message.replace("%spieler%", playerName)
                .replace("%targetPlayer%", targetName)
                .replace("%reson%", (reson != null ? reson : "No reason provided"))
                .replace("%prefix%", (BanPlugin.prefixMiniMessage != null ? BanPlugin.prefixMiniMessage : ""));
    }

    private void PlayerKick(Player player){
        player.disconnect(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.KickMessage)));
    }

    private void BanPlayer(Player targetPlayer, Player player, String ipaddress, LocalDateTime timenow, String reson, int timehour){
        if (Integer.parseInt(Ban.GetPoints(player)) < (Integer.parseInt(BanPlugin.MaxPoins))) {
            Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, BanPlugin.MaxPoinsReason, "Server", Integer.parseInt(BanPlugin.Bantime));
        } else {
            Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, reson, "Server",87660);
        }

        PlayerKick(targetPlayer);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnBan)));
    }

    private void ChatBanPlayer(Player targetPlayer, Player player,String ipaddress, LocalDateTime timenow, String reson, int timehour){
        Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, reson, "Chat", timehour);
        Banlog.AddBanLog(targetPlayer, player, reson, timenow);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnChatban)));
    }
}
