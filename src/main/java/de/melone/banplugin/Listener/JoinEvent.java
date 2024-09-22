package de.melone.banplugin.Listener;

import com.mongodb.client.model.Filters;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;

import java.time.LocalDateTime;
import java.util.Objects;

import static javax.management.Query.eq;

public class JoinEvent {

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();

        if (BanlogSQL.isMongoDBConnected(BanlogSQL.mongoClient) || BanSQL.isMongoDBConnected(BanSQL.mongoClient)){

            Document doc = BanSQL.collection.find(Filters.eq("_id", player.getUniqueId().toString())).first();
            if (!(doc == null)){

                String date = doc.getString("Time");
                int dauer = doc.getInteger("Hours");

                LocalDateTime now = LocalDateTime.now();
                LocalDateTime time = now.minusHours(dauer);

                LocalDateTime playertime = LocalDateTime.parse(date);

                if (time.isBefore(playertime)) {
                    String reson = doc.getString("reson");

                    player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.type1));

                   if (reson.equals(BanPlugin.reson1)){
                       player.sendMessage(MiniMessage.miniMessage().deserialize("test1"));
                       if (BanPlugin.type1.equals("Server")){
                           player.sendMessage(MiniMessage.miniMessage().deserialize("test2"));
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson2)){
                       if (BanPlugin.type2.equals("Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson3)){
                       if (Objects.equals(BanPlugin.type3, "Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson4)){
                       if (Objects.equals(BanPlugin.type4, "Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson5)){
                       if (BanPlugin.type5.equals("Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson6)){
                       if (BanPlugin.type6.equals("Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (BanPlugin.reson7 == reson){
                       if (BanPlugin.type7.equals("Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }

                   } else if (reson.equals(BanPlugin.reson8)){
                       if (BanPlugin.type8.equals("Server")){
                           BanSQL.GetPlayerBan(player);
                       } else {
                           BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                           BanSQL.GetPlayerChatBan(player);
                       }
                   }
                }
            }
        } else {
            player.disconnect(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.ConnectionERROR)));
        }
    }

    private static String ConfigMessages(String message) {
        if (message.contains("%prefix%")) {
            return message.replace("%prefix%", BanPlugin.prefixMiniMessage);
        }
        return message;
    }
}