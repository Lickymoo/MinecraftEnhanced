package com.buoobuoo.minecraftenhanced.persistence;

import com.buoobuoo.minecraftenhanced.MinecraftEnhanced;
import com.buoobuoo.minecraftenhanced.core.event.DatabaseConnectEvent;
import com.buoobuoo.minecraftenhanced.core.permission.PermissionGroup;
import com.buoobuoo.minecraftenhanced.persistence.serialization.DoNotSerialize;
import com.buoobuoo.minecraftenhanced.persistence.serialization.VariableSerializer;
import com.buoobuoo.minecraftenhanced.persistence.serialization.impl.ItemStackSerializer;
import com.buoobuoo.minecraftenhanced.persistence.serialization.impl.LocationSerializer;
import com.buoobuoo.minecraftenhanced.persistence.serialization.impl.PermissionGroupSerializer;
import com.buoobuoo.minecraftenhanced.persistence.serialization.impl.UUIDSerializer;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MongoHook {

    private final MinecraftEnhanced plugin;

    MongoCollection<Document> collection = null;
    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    private static final String ip = "cluster0.kqea6ig.mongodb.net";
    private static final String pw = "ZbCd5o3HHSHoQpqh";

    private static final String defaultDB = "test";

    private final HashMap<Class<?>, VariableSerializer<?>> variableSerializers = new HashMap<>();

    public MongoHook(MinecraftEnhanced plugin) {
        this.plugin = plugin;
        registerSerializer(UUID.class, new UUIDSerializer());
        registerSerializer(ItemStack[].class, new ItemStackSerializer());
        registerSerializer(Location.class, new LocationSerializer());
        registerSerializer(PermissionGroup.class, new PermissionGroupSerializer());

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::init, 1);
    }

    private void init() {
        try {
            Bukkit.getConsoleSender().sendMessage("Connecting to MongoDB");

            ConnectionString connectionString = new ConnectionString("mongodb://minecraftenhanced:"+pw+"@ac-ug8goy4-shard-00-00.kqea6ig.mongodb.net:27017,ac-ug8goy4-shard-00-01.kqea6ig.mongodb.net:27017,ac-ug8goy4-shard-00-02.kqea6ig.mongodb.net:27017/?ssl=true&replicaSet=atlas-f24p57-shard-0&authSource=admin&retryWrites=true&w=majority");
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(connectionString)
                    .build();

            mongoClient = MongoClients.create(settings);

            mongoDatabase = mongoClient.getDatabase(defaultDB);
            collection = mongoDatabase.getCollection("players");
            Bukkit.getConsoleSender().sendMessage("Successfully connected to MongoDB");
            Bukkit.getScheduler().runTaskLater(plugin, () -> {Bukkit.getServer().getPluginManager().callEvent(new DatabaseConnectEvent());}, 1);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void disable() {
        mongoClient.close();
    }

    public void registerSerializer(Class<?> clazz, VariableSerializer<?> serializer){
        variableSerializers.put(clazz, serializer);
    }

    //Util
    public boolean valueExists(String varName, String varValue, String collectionName) {
        this.collection = mongoDatabase.getCollection(collectionName);
        Document document = collection.find(new Document(varName, varValue)).first();
        return document != null;
    }

    @SuppressWarnings("unchecked")
    public <T> VariableSerializer<Object> getSerializer(Class<T> clazz){
        return (VariableSerializer<Object>) variableSerializers.getOrDefault(clazz, null);
    }

    public void deleteObject(String id, String collectionName) {
        collection = mongoDatabase.getCollection(collectionName);
        Document document = new Document("_id", id);
        collection.deleteOne(document);
    }


    public void saveObject(String id, Object object, String collectionName) {
        collection = mongoDatabase.getCollection(collectionName);

        Map<String, Object> variableMap = new HashMap<>();


        for(Field field : object.getClass().getDeclaredFields()) {
            try {
                //disable meta fields
                if (field.getName().contains("SWITCH_TABLE")) continue;
                if (field.getName().contains("ANNOTATION")) continue;
                if (field.getName().contains("ENUM")) continue;
                if (field.getName().contains("SYNTHETIC")) continue;

                field.setAccessible(true);

                //switches get saved as variables, so ignore that
                if (field.getAnnotation(DoNotSerialize.class) != null) continue;

                VariableSerializer<Object> serializer = getSerializer(field.getType());
                Object value = field.get(object);
                if (serializer != null && field.get(object) != null) {
                    value = serializer.serialize(field.get(object));
                }

                variableMap.put(field.getName(), value);
            }catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        Document document = collection.find(new Document("_id", id)).first();
        if(document == null){
            Document newValue = new Document("_id", id);
            collection.insertOne(newValue);
            document = collection.find(new Document("_id", id)).first();
        }
        for(Map.Entry<String, Object> entrySet : variableMap.entrySet()) {
            if(entrySet.getValue() == null) continue;
            Bson bsonValue = new Document(entrySet.getKey(), entrySet.getValue());
            Bson bsonOperation = new Document("$set", bsonValue);
            collection.updateOne(document, bsonOperation);
        }
    }

    public <T> T loadObject(String id, Class<T> clazz, String collectionName) {
        return loadObject(id, "_id", clazz, collectionName);

    }

    public <T> T loadObject(String searchValue, String columnName, Class<T> clazz, String collectionName){
        collection = mongoDatabase.getCollection(collectionName);

        T object = null;
        try {
            object = clazz.getDeclaredConstructor().newInstance();
            Document document = collection.find(new Document(columnName, searchValue)).first();

            if(document == null) return object;
            for(Field field : clazz.getDeclaredFields()) {
                try {
                    if(field == null) continue;
                    if(field.getName().isEmpty()) continue;

                    field.setAccessible(true);
                    Object value;

                    if(field.getAnnotation(DoNotSerialize.class) != null) continue;

                    VariableSerializer<Object> serializer = getSerializer(field.getType());
                    if (serializer != null && document.getString(field.getName()) != null) {
                        String strVal = document.getString(field.getName());
                        value = serializer.deserialize(strVal);
                    }else{
                        value = document.get(field.getName());
                    }

                    field.set(object, value);

                } catch (IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }catch(IllegalAccessException | NoSuchMethodException  | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        return object;
    }
}























