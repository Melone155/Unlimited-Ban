package de.melone.banplugin.Listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import de.melone.banplugin.ulti.BanSQL;

public class PlayerChat {

    @Subscribe
    public void onPlayerChat(PlayerChatEvent event) {
        Player player = event.getPlayer();

        if (BanPlugin.playerChatAllow.contains(player.getUniqueId().toString())) {
            event.setResult(PlayerChatEvent.ChatResult.denied());
            BanSQL.GetPlayerChatBan(player);
        }
    }
}
