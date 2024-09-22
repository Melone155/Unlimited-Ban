package de.melone.banplugin.cmd;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            player = (Player) source;
            if (player.hasPermission("Ban.ban")) {
                if (invocation.arguments().length == 2) {
                    String playerName = args[0];
                    String argument = args[1];

                    Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                    if (optionalPlayer.isPresent()) {
                        targetPlayer = optionalPlayer.get();
                        LocalDateTime timenow = LocalDateTime.now();

                        if (argument.equals("1")) {
                            if (BanPlugin.type1 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson1, Integer.valueOf(BanPlugin.time1));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson1, Integer.valueOf(BanPlugin.time1));
                            }

                        } else if (argument.equals("2")) {
                            if (BanPlugin.type2 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson2, Integer.valueOf(BanPlugin.time2));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson2, Integer.valueOf(BanPlugin.time2));
                            }

                        } else if (argument.equals("3")) {
                            if (BanPlugin.type3 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson3, Integer.valueOf(BanPlugin.time3));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson3, Integer.valueOf(BanPlugin.time3));
                            }

                        } else if (argument.equals("4")) {
                            if (BanPlugin.type4 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson4, Integer.valueOf(BanPlugin.time4));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson4, Integer.valueOf(BanPlugin.time4));
                            }

                        } else if (argument.equals("5")) {
                            if (BanPlugin.type5 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson5, Integer.valueOf(BanPlugin.time5));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson5, Integer.valueOf(BanPlugin.time5));
                            }

                        } else if (argument.equals("6")) {
                            if (BanPlugin.type6 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson6, Integer.valueOf(BanPlugin.time6));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson6, Integer.valueOf(BanPlugin.time6));
                            }

                        } else if (argument.equals("7")) {
                            if (BanPlugin.type7 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson7, Integer.valueOf(BanPlugin.time7));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson7, Integer.valueOf(BanPlugin.time7));
                            }

                        } else if (argument.equals("8")) {
                            if (BanPlugin.type8 == "Server") {
                                BanPlayer(targetPlayer, player, timenow, BanPlugin.reson8, Integer.valueOf(BanPlugin.time8));
                            } else {
                                ChatBanPlayer(targetPlayer, player, timenow, BanPlugin.reson8, Integer.valueOf(BanPlugin.time8));
                            }
                        }

                    } else {
                        player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.PlayerNotFound));
                    }
                } else if (invocation.arguments().length == 1) {
                    String argument = args[0];
                    if (argument.equals("list")) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("========== " + BanPlugin.prefixMiniMessage + " ==========" +
                                "<newline>1. " + BanPlugin.reson1 +
                                "<newline>2. " + BanPlugin.reson2 +
                                "<newline>3. " + BanPlugin.reson3 +
                                "<newline>4. " + BanPlugin.reson4 +
                                "<newline>5. " + BanPlugin.reson5 +
                                "<newline>6. " + BanPlugin.reson6 +
                                "<newline>7. " + BanPlugin.reson7 +
                                "<newline>8. " + BanPlugin.reson8
                        ));
                    }
                    if (argument.equals("help")) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("========== " + BanPlugin.prefixMiniMessage + " ==========" +
                                "<newline> /ban <Player> 1,2,3..." +
                                "<newline /ban <Player> <time in Hours> <reson>" +
                                "<newline> /unban <Player>"));
                    } else {
                        player.sendMessage(Component.text("/ban <Player> 1,2,3..."));
                    }
                    if (invocation.arguments().length == 3) {
                        if (player.hasPermission("Ban.BanAdmin")) {
                            String playerName = args[0];
                            String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
                            String bantime = args[1];

                            Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                            if (optionalPlayer.isPresent()) {
                                targetPlayer = optionalPlayer.get();
                                LocalDateTime timenow = LocalDateTime.now();

                                BanPlayer(targetPlayer, player, timenow, message, Integer.valueOf(bantime));
                            }
                        } else {
                            player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.noperms)));
                        }
                    } else {
                        player.sendMessage(Component.text("/ban <Player> 1,2,3..."));
                    }
                }
            } else {
                player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.noperms)));
            }
        }
    }

    private void PlayerKick(Player player){
        player.disconnect(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.KickMessage)));
    }

    private void BanPlayer(Player targetPlayer, Player player,LocalDateTime timenow, String reson, int timeinmin){
        if (Integer.valueOf(BanSQL.GetPoints(player)) == (Integer.valueOf(BanPlugin.MaxPoins) -1)) {
            BanSQL.CreatePlayerBan(targetPlayer, timenow, BanPlugin.MaxPoinsReason, Integer.valueOf(BanPlugin.Bantime));
        } else {
            BanSQL.CreatePlayerBan(targetPlayer, timenow, reson, timeinmin);
        }
        BanlogSQL.CreatePlayerBan(targetPlayer, player, reson, timenow);
        PlayerKick(targetPlayer);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnBan)));
    }

    private void ChatBanPlayer(Player targetPlayer, Player player,LocalDateTime timenow, String reson, int dauerinTage){
        BanSQL.CreatePlayerBan(targetPlayer, timenow, reson, dauerinTage);
        BanlogSQL.CreatePlayerBan(targetPlayer, player, reson, timenow);
        player.sendMessage(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ReturnChatban)));
    }

    private static String ConfigMessages(String message) {
        String targetName = (targetPlayer != null) ? targetPlayer.getUsername() : "Unknown";
        String playerName = (player != null) ? player.getUsername() : "Unknown";

        return message.replace("%spieler%", playerName)
                .replace("%targetPlayer%", targetName)
                .replace("%reson%", (reson != null ? reson : "No reason provided"))
                .replace("%prefix%", (BanPlugin.prefixMiniMessage != null ? BanPlugin.prefixMiniMessage : ""));
    }


}