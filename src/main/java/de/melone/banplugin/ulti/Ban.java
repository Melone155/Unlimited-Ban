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
import org.bson.Document;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.mongodb.client.model.Filters.eq;

public class Ban {

    public static MongoCollection<Document> collection;
    public static MongoClient mongoClient;
    private static final String uri = "mongodb://" + BanPlugin.bansUsername + ":" + BanPlugin.bansPassword + "@" + BanPlugin.bansHost + ":" + BanPlugin.bansPort + "/Ban?authSource=Ban";

    public static void ConnectionBan() {

        mongoClient = MongoClients.create(uri);
        MongoDatabase database = mongoClient.getDatabase(BanPlugin.bansDatabase);
        collection = database.getCollection(BanPlugin.bansCollection);

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

    public static void CreatePlayerBan(Player player, LocalDateTime localDateTime, String reson, int bandauer) {

        if (!isMongoDBConnected(mongoClient)) {
            player.sendMessage(Component.text(BanPlugin.prefixMiniMessage + "Fehler: 425 Bitte Kontaktieren sie einen Admin"));
        } else {

            Document doc = collection.find(eq("_id", player.getUniqueId().toString())).first();

            LocalDateTime time = localDateTime.plusDays(bandauer);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String formatDateTime = time.format(formatter);

            if (doc == null) {
                InsertOneResult result = collection.insertOne(new Document()
                        .append("_id", player.getUniqueId().toString())
                        .append("name", player.getGameProfile().getName())
                        .append("reson", reson)
                        .append("Time", localDateTime.toString())
                        .append("Hours", bandauer)
                        .append("Type", "")
                        .append("Timeforplayer", formatDateTime));
            }
        }
    }

    public static String GetPoints(Player player) {
        Document doc = collection.find(eq("_id", player.getUniqueId().toString())).first();
        assert doc != null;
        return doc.getString("Punkte");
    }
}
