package tools.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoUtil {
    public static MongoDatabase getMongoDataBase (){
        //MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://zhao:pc3r-2021@64.225.1.187:27017/?authSource=zhao-pc3r"));
        ConnectionString connectionString = new ConnectionString("mongodb://zhao:pc3r-2021@64.225.1.187:27017/?authSource=zhao-pc3r");
        CodecRegistry pojoCodecRegistry = fromProviders(PojoCodecProvider.builder().automatic(true).build());
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                pojoCodecRegistry);
        MongoClientSettings clientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .codecRegistry(codecRegistry)
                .build();
        MongoClient mongoClient = MongoClients.create(clientSettings);
        return mongoClient.getDatabase("zhao-pc3r");
    }
}
