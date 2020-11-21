package com.bigjpg.model.rest;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * GsonDeserializeListener
 * @author Momo
 */
public interface GsonDeserializeListener {
    void onDeserialize(Gson gson, JsonElement json, Type typeOfT, JsonDeserializationContext context);
}
