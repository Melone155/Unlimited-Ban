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
import java.util.Optional;

public class CMD_ban implements SimpleCommand {

    private final ProxyServer proxy;

    public CMD_ban(ProxyServer proxy) {
        this.proxy = proxy;
    }

    @Override
    public void execute(final Invocation invocation) {
        CommandSource source = invocation.source();
        String[] args = invocation.arguments();

        if (source instanceof Player) {
            Player player = (Player) source;
                if (invocation.arguments().length == 2) {
                    String playerName = args[0];
                    String argument = args[1];

                    Optional<Player> optionalPlayer = proxy.getPlayer(playerName);
                    if (optionalPlayer.isPresent()) {
                        Player targetPlayer = optionalPlayer.get();
                        LocalDateTime timenow = LocalDateTime.now();

                        if (argument.equals("1")) {
                            BanPlayer(targetPlayer,player, timenow, "Hacking", BanPlugin.Hacking);

                        } else  if (argument.equals("2")) {
                            ChatBanPlayer(targetPlayer,player, timenow, "Werbung", BanPlugin.AD);
                            BanPlugin.playerChatAllow.add(targetPlayer.getUniqueId().toString());

                        } else  if (argument.equals("3")) {
                            ChatBanPlayer(targetPlayer,player, timenow, "Spam", BanPlugin.Spam);
                            BanPlugin.playerChatAllow.add(targetPlayer.getUniqueId().toString());

                        } else  if (argument.equals("4")) {
                            ChatBanPlayer(targetPlayer,player, timenow, "Beleidigung", BanPlugin.insult);
                            BanPlugin.playerChatAllow.add(targetPlayer.getUniqueId().toString());

                        } else  if (argument.equals("5")) {
                            BanPlayer(targetPlayer,player, timenow, "Bugusing", BanPlugin.Bugusing);

                        } else  if (argument.equals("6")) {
                            BanPlayer(targetPlayer,player, timenow, "Skin", BanPlugin.Skin);

                        } else  if (argument.equals("7")) {
                            BanPlayer(targetPlayer,player, timenow, "Hatespeech/Diskriminierung/Rassismus", BanPlugin.Hatespeech);

                        } else  if (argument.equals("8")) {
                            BanPlayer(targetPlayer,player, timenow, "Verbotene Buildings", BanPlugin.Illegal);

                        } else if (argument.equals("log")) {
                            BanlogSQL.GetPlayerBans(player, targetPlayer.getUniqueId());
                        }

                    } else {
                        player.sendMessage(Component.text("Spieler nicht gefunden."));
                    }
                } else if (invocation.arguments().length == 1) {
                    String argument = args[0];
                    if (argument.equals("list")) {
                        player.sendMessage(MiniMessage.miniMessage().deserialize("========== " + BanPlugin.prefixMiniMessage + " ==========" +
                                "<newline>1. Hacking" +
                                "<newline>2. Werbung" +
                                "<newline>3. Spam" +
                                "<newline>4. Beleidigung" +
                                "<newline>5. Bugusing" +
                                "<newline>6. Skin" +
                                "<newline>7. Hatespeech/Diskriminierung/Rassismus" +
                                "<newline>8. Verbotene Buildings"
                        ));
                    } else {
                        player.sendMessage(Component.text("/ban <Player> 1,2,3..."));
                    }
                } else {
                    player.sendMessage(Component.text("/ban <Player> 1,2,3..."));
                }
        }
    }

    private void PlayerKick(Player player){
        player.disconnect(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + "<newline> Du Wurdest verwarnt/gebannt bitte Join Neu für mehr Infos"));
    }

    private void BanPlayer(Player targetPlayer, Player player,LocalDateTime timenow, String reson, int timeinmin){
        BanSQL.CreatePlayerBan(targetPlayer, timenow, reson, timeinmin);
        BanlogSQL.CreatePlayerBan(targetPlayer, player, reson, timenow);
        PlayerKick(targetPlayer);
        player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Du hast den Spieler " + targetPlayer.getGameProfile().getName().toString() + " vom Server gebannt wegen " + reson));
    }

    private void ChatBanPlayer(Player targetPlayer, Player player,LocalDateTime timenow, String reson, int dauerinTage){
        BanSQL.CreatePlayerBan(targetPlayer, timenow, reson, dauerinTage);
        BanlogSQL.CreatePlayerBan(targetPlayer, player, reson, timenow);
        player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Du hast den Spieler " + targetPlayer.getGameProfile().getName().toString() + " auf dem Chat gebannt wegen " + reson));
    }
}
