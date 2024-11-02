package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.Ban;
import de.melone.banplugin.ulti.Banlog;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.util.Optional;

public class CMD_ban implements SimpleCommand {

    private final ProxyServer proxy;
    private static Player player;
    private static Player targetPlayer;
    private static String reson;

    public CMD_ban(ProxyServer proxy) {
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

        player = (Player) source;
        if (!player.hasPermission("Ban.ban")) {
         player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + ConfigMessages(BanPlugin.noperms)));
         return;
        }

        if (invocation.arguments().length == 2) {
            String playerName = args[0];
            String argument = args[1];

            Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
            Optional<InetSocketAddress> remoteAddress = Optional.ofNullable(player.getRemoteAddress());
            if (optionalPlayer.isPresent()) {
                if (remoteAddress.isPresent()) {
                    targetPlayer = optionalPlayer.get();
                    LocalDateTime timenow = LocalDateTime.now();
                    String PlayerIPAddress = remoteAddress.get().getAddress().getHostAddress();

                    switch (argument) {
                        case "1":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson1, Integer.parseInt(BanPlugin.time1));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson1, Integer.parseInt(BanPlugin.time1));
                            }
                            break;

                        case "2":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson2, Integer.parseInt(BanPlugin.time2));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson2, Integer.parseInt(BanPlugin.time2));
                            }
                            break;

                        case "3":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson3, Integer.parseInt(BanPlugin.time3));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson3, Integer.parseInt(BanPlugin.time3));
                            }
                            break;

                        case "4":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson4, Integer.parseInt(BanPlugin.time4));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson4, Integer.parseInt(BanPlugin.time4));
                            }
                            break;

                        case "5":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson5, Integer.parseInt(BanPlugin.time5));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson5, Integer.parseInt(BanPlugin.time5));
                            }
                            break;

                        case "6":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson6, Integer.parseInt(BanPlugin.time6));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson6, Integer.parseInt(BanPlugin.time6));
                            }
                            break;

                        case "7":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson7, Integer.parseInt(BanPlugin.time7));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson7, Integer.parseInt(BanPlugin.time7));
                            }
                            break;

                        case "8":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson8, Integer.parseInt(BanPlugin.time8));
                            } else {
                                ChatBanPlayer(targetPlayer, player, PlayerIPAddress, timenow, BanPlugin.reson8, Integer.parseInt(BanPlugin.time8));
                            }
                            break;

                        case "9":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson9, Integer.parseInt(BanPlugin.time9));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson9, Integer.parseInt(BanPlugin.time9));
                            }
                            break;

                        case "10":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson10, Integer.parseInt(BanPlugin.time10));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson10, Integer.parseInt(BanPlugin.time10));
                            }
                            break;

                        case "11":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress,timenow, BanPlugin.reson11, Integer.parseInt(BanPlugin.time11));
                            } else {
                                ChatBanPlayer(targetPlayer, player,  PlayerIPAddress,timenow, BanPlugin.reson11, Integer.parseInt(BanPlugin.time11));
                            }
                            break;

                        case "12":
                            if (BanPlugin.type1.equals("Server")) {
                                BanPlayer(targetPlayer, player, PlayerIPAddress, timenow, BanPlugin.reson12, Integer.parseInt(BanPlugin.time12));
                            } else {
                                ChatBanPlayer(targetPlayer, player, PlayerIPAddress, timenow, BanPlugin.reson12, Integer.parseInt(BanPlugin.time12));
                            }
                            break;
                    }
                }
            }
        } else {
            player.sendMessage(MiniMessage.miniMessage().deserialize("========== " + BanPlugin.prefixMiniMessage + " ==========" +
                    "<newline> /ban <Player> 1,2,3..." +
                    "<newline /ban <Player> <time in Hours> <reson>" +
                    "<newline> /unban <Player>"));
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

    private void BanPlayer(Player targetPlayer, Player player, String ipaddress,LocalDateTime timenow, String reson, int timehour){
        if (Integer.parseInt(Ban.GetPoints(player)) < (Integer.parseInt(BanPlugin.MaxPoins))) {
            Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, BanPlugin.MaxPoinsReason, "Server", Integer.parseInt(BanPlugin.Bantime));
        } else {
            Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, reson, "Server",87660);
        }

        PlayerKick(targetPlayer);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnBan)));
    }

    private void ChatBanPlayer(Player targetPlayer, Player player,String ipaddress, LocalDateTime timenow, String reson, int dauerinTage){
        Ban.CreatePlayerBan(targetPlayer, ipaddress, timenow, reson, "Chat", dauerinTage);
        Banlog.AddBanLog(targetPlayer, player, reson, timenow);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnChatban)));
    }
}