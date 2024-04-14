package de.melone.banplugin.Listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.ulti.BanSQL;
import de.melone.banplugin.ulti.GetBanSQL;
import net.kyori.adventure.text.Component;

public class JoinEvent {

    @Subscribe
    public void onPlayerJoin(PlayerChooseInitialServerEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(Component.text("test"));
        BanSQL.GetPlayerBan(player);
    }
}
