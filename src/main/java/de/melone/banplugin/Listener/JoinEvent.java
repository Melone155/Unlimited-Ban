package de.melone.banplugin.Listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.BanlogSQL;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class JoinEvent {

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        if (BanlogSQL.isMongoDBConnected(BanlogSQL.mongoClient) == true && BanSQL.isMongoDBConnected(BanSQL.mongoClient) == true){
            BanSQL.GetPlayerBan(player);
        } else {
            player.disconnect(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + "<newlien> leider ist uns ein Fehler unterlaufen <newline> Bitte informieren sie das Server-Team, um diesen Fehler zu beheben."));
        }
    }
}
