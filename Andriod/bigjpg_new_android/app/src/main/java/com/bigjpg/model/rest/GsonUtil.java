package com.bigjpg.model.rest;

import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.response.UpgradeResponse;
import com.bigjpg.model.response.UserResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * 描述: GsonUtil
 *
 * @author mfx
 * @date 2016-05-12 0:00
 */
public class GsonUtil {

    private static Gson sGson;

    public static Gson getGson() {
        if (sGson == null) {
            synchronized (GsonUtil.class) {
                sGson = createGsonBuilder().create();
            }
        }
        return sGson;
    }

    private static GsonBuilder createGsonBuilder(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        CustomDeserializer logDeserializer = new CustomDeserializer(EnlargeLog.class);
        CustomDeserializer userDeserializer = new CustomDeserializer(UserResponse.class);
        CustomDeserializer upgradeDeserializer = new CustomDeserializer(UpgradeResponse.class);
        gsonBuilder.registerTypeAdapter(UserResponse.class, userDeserializer);
        gsonBuilder.registerTypeAdapter(EnlargeLog.class, logDeserializer);
        gsonBuilder.registerTypeAdapter(UpgradeResponse.class, upgradeDeserializer);
//        BooleanSerializer serializer = new BooleanSerializer();
//        gsonBuilder.registerTypeAdapter(Boolean.class, serializer);
//        gsonBuilder.registerTypeAdapter(boolean.class, serializer);
//        gsonBuilder.registerTypeAdapterFactory(new DeserializeActionAdapterFactory());
        return gsonBuilder;
    }

    private static class CustomDeserializer<T extends GsonDeserializeListener> implements JsonDeserializer {
        private Class<T> tClass;
        private Gson gson;

        public CustomDeserializer(Class<T> tClass){
            this.tClass = tClass;
            gson = new Gson();
        }

        @Override
        public  T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            T data =  gson.fromJson(json, tClass);
            data.onDeserialize(gson, json, typeOfT, context);
            return data;
        }
    }


    private static class BooleanSerializer implements JsonSerializer<Boolean>, JsonDeserializer<Boolean> {

        @Override
        public JsonElement serialize(Boolean arg0, Type arg1, JsonSerializationContext arg2) {
            return new JsonPrimitive(arg0 ? 1 : 0);
        }

        @Override
        public Boolean deserialize(JsonElement arg0, Type arg1, JsonDeserializationContext arg2) throws JsonParseException {
            return arg0.getAsInt() == 1;
        }

    }

}
