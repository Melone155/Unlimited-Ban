package de.melone.banplugin.ulti;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import com.velocitypowered.api.proxy.Player;
import de.melone.banplugin.BanPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static com.mongodb.client.model.Filters.eq;

public class BanSQL {

    private static MongoCollection<Document> collection;
    private static MongoClient mongoClient;
    private static String uri = "mongodb://banuser:uAjg%5EbD%26gMcF9McCeiAymNneWkUt8mLh%26PqD2Fo2ynN8wNguUpVkh%23%40DGMx%26W8xXng2nYUphVr5rm6*rcZP3YRN%23b%5E%26Qpn%5EL5!EqmN7M79P3nDGQvqLPowar4!svC4Jj@185.239.238.243:27017/?authMechanism=SCRAM-SHA-1&authSource=Ban";

    public static void ConnectionBan() {

        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase("Ban");
        collection = database.getCollection("bans");

    }

    public static boolean isMongoDBConnected(MongoClient mongoClient) {
        try {
            mongoClient.listDatabaseNames();

            return true;
        } catch (MongoException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void CreatePlayerBan(Player player, LocalDateTime localDateTime, String reson, int bandauer){

        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefix + "Fehler: 425 Bitte Kontaktieren sie einen Admin"));
        } else {
            System.out.println("Erfolgreich mit der MongoDB-Datenbank verbunden.");

            Document doc = collection.find(eq("_id", player.getUniqueId().toString())).first();

            LocalDateTime time = localDateTime.plusDays(bandauer);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formatDateTime = time.format(formatter);

            if (doc == null){
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", player.getUniqueId().toString())
                        .append("name", player.getGameProfile().getName())
                        .append("reson", reson)
                        .append("Time", localDateTime.toString())
                        .append("Days", bandauer)
                        .append("Timeforplayer", formatDateTime));
            }
        }
    }

    public static void GetPlayerBan(Player player){
        Document doc = collection.find(eq("_id", player.getUniqueId().toString())).first();

        if (doc != null){
            String date = doc.getString("Time");
            String reson = doc.getString("reson");
            String datumentbann = doc.getString("Timeforplayer");
            int dauer = doc.getInteger("Days");


            LocalDateTime now = LocalDateTime.now();
            LocalDateTime time = now.minusHours(dauer);

            LocalDateTime playertime = LocalDateTime.parse(date);

            if (time.isBefore(playertime)) {
             switch (reson){
                 case "Werbung":
                     BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                     break;
                 case "Spam":
                     BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                     break;
                 case "Beleidigung":
                     BanPlugin.playerChatAllow.add(player.getUniqueId().toString());
                 default:
                     player.disconnect(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage +
                             "<newline> Du Wurdest von unserem Netzwerk Gebannt" +
                             "<newline> Grund: " + reson +
                             "<newline> Bis zum " + datumentbann +
                             "<newline> Sollte es ihr ein mis verstehnis geben ehe auf fuchscraft.de/entbannung"));
                     break;
             }
            } else {
                collection.deleteOne(eq("_id", player.getUniqueId().toString()));
            }
        }
    }

    public static void GetPlayerChatBan(Player player){
        Document doc = collection.find(eq("_id", player.getUniqueId().toString())).first();
        String reson = doc.getString("reson");
        String datumentbann = doc.getString("Timeforplayer");

        player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + "Du wurdest wegen " + reson + " aus dem Chat gebannt bis zum " + datumentbann));
    }

    public static void Playerunban(Player targetPlayer, Player player){
        collection.deleteOne(eq("_id", targetPlayer.getUniqueId().toString()));
        player.sendMessage(MiniMessage.miniMessage().deserialize(BanPlugin.prefixMiniMessage + "Du hast den Spieler " + targetPlayer.getGameProfile().getName() +  "Entbannt"));
    }
}
