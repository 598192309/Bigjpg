package com.bigjpg.model.response;

import com.bigjpg.model.entity.EnlargeLog;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.rest.GsonDeserializeListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserResponse extends HttpResponse implements GsonDeserializeListener {

    private static final long serialVersionUID = 1L;

    private User user;

    private List<EnlargeLog> logs_;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<EnlargeLog> getLogs() {
        return logs_;
    }

    public void setLogs(List<EnlargeLog> logs_) {
        this.logs_ = logs_;
    }

    @Override
    public void onDeserialize(Gson gson, JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        JsonObject object = json.getAsJsonObject();
        JsonArray array = object.getAsJsonArray("logs");
        if(array != null && array.size() > 0){
            try {
                logs_ = new ArrayList<>();
                for(int i = 0; i < array.size(); i++){
                    JsonArray itemArray = array.get(i).getAsJsonArray();
                    EnlargeLog log = new EnlargeLog();
                    log.create(itemArray);
                    logs_.add(log);
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
