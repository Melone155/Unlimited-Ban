package de.melone.banplugin.Listiner;

import com.velocitypowered.api.event.ResultedEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.Ban;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;

public class JoinQuit {

    @Subscribe
    public void onPlayerJoin(PostLoginEvent event) {
        Player player = event.getPlayer();
        String ipaddress = player.getRemoteAddress().toString();

        if (Ban.isPlayerBan(player, ipaddress)) {
            Document doc = Ban.collection.find(eq("_id", player.getUniqueId().toString())).first();

            String reason = doc != null ? doc.getString("reason") : null;
            String time = doc != null ? doc.getString("Timeforplayer") : null;

            ResultedEvent.ComponentResult.denied(MiniMessage.miniMessage().deserialize(ConfigMessages(BanPlugin.banscreen, reason, time)));
        }
    }

    private static String ConfigMessages(String message, String reason, String time) {
        return message.replace("%reason%", reason)
                .replace("%prefix%", (BanPlugin.prefixMiniMessage != null ? BanPlugin.prefixMiniMessage : "")
                        .replace("%time%", time));
    }
}
