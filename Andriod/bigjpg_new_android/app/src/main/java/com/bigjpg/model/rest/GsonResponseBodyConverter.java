package com.bigjpg.model.rest;

import com.bigjpg.model.response.IOriginResponse;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;


public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;
    private final JsonParser parser;

    GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
        parser = new JsonParser();
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        Reader reader = value.charStream();
        try {
            JsonElement element = parser.parse(reader);
            JsonObject object = element.getAsJsonObject();
            T response = gson.fromJson(object, type);
            if(response instanceof IOriginResponse){
                IOriginResponse originResponse = (IOriginResponse)response;
                originResponse.setOriginData(object.toString());
            }
            return response;
        } finally {
            Utils.closeQuietly(reader);
        }
    }

}
