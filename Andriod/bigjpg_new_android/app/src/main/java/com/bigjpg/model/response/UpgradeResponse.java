package com.bigjpg.model.response;

import android.app.Activity;
import android.content.res.Configuration;

import com.bigjpg.model.entity.UpgradeItem;
import com.bigjpg.model.entity.User;
import com.bigjpg.model.rest.GsonDeserializeListener;
import com.bigjpg.util.AppManager;
import com.bigjpg.util.ResourcesUtil;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UpgradeResponse extends HttpResponse implements GsonDeserializeListener {

    private static final long serialVersionUID = 1L;

    private List<UpgradeItem> list_;

    @Override
    public void onDeserialize(Gson gson, JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Activity activity = AppManager.getInstance().getCurrentActivity();
        if (activity != null) {
            Configuration configuration = ResourcesUtil.getConfiguration(activity);
            Locale locale = configuration.locale;
            if (locale != null) {
                String language = locale.getLanguage();
                if (language.endsWith("zh")) {
                    if ("TW".equalsIgnoreCase(locale.getCountry())) {
                        handleData(json, "tw");
                    } else {
                        handleData(json, "zh");
                    }
                } else if (language.endsWith("ja")) {
                    handleData(json, "jp");
                } else if (language.endsWith("en")) {
                    handleData(json, "en");
                } else if (language.endsWith("ru")) {
                    handleData(json, "ru");
                } else if (language.endsWith("zh_rTW")) {
                    handleData(json, "tw");
                } else {
                    handleData(json, "en");
                }
            }
        }
    }

    private void handleData(JsonElement json, String language) {
        JsonObject jsonObject = json.getAsJsonObject();
        jsonObject = jsonObject.getAsJsonObject("lng_dict");
        jsonObject = jsonObject.getAsJsonObject("func");
        JsonArray jsonArray = jsonObject.getAsJsonArray(language);
        list_ = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonArray array = jsonArray.get(i).getAsJsonArray();
            UpgradeItem item = new UpgradeItem();
            if (i == 0) {
                item.version = User.Version.NONE;
            } else if (i == 1) {
                item.version = User.Version.BASIC;
            } else if (i == 2) {
                item.version = User.Version.STANDARD;
            } else if (i == 3) {
                item.version = User.Version.PRO;
            }
            item.title = array.get(0).getAsString();
            item.content = new ArrayList<>();
            for (int j = 1; j < array.size(); j++) {
                item.content.add(array.get(j).getAsString());
            }
            list_.add(item);
        }
    }

    public List<UpgradeItem> getList() {
        return list_;
    }

    public void setList(List<UpgradeItem> list_) {
        this.list_ = list_;
    }
}
