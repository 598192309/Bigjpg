package com.bigjpg.model.entity;

import com.bigjpg.model.rest.GsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * 放大记录
 * <p>
 * {conf放大参数, output放大后地址, 创建时间, 状态, fid}
 *
 * "[
 * {"files_size":27445,"style":"art","noise":"-1","file_name":"微信图片_20190414190739.jpg","file_height":328,"file_width":317,"time":1,"x2":"1","input":"https://bigjpg-upload.oss-cn-shenzhen.aliyuncs.com/upload/2019-4-14/15552400679960.5596730703241608.jpg"},
 * "https://cdn-ossd.zipjpg.com/pay/2f801e32580d7e82069710ff92e2a704_1_-1_art.jpg",
 * "2019-04-14 19:07:53",
 * "success",
 * "786e301f90f841ee82c6e237d8814bab"]]
 */
public class EnlargeLog implements IEntity{

    private static final long serialVersionUID = 1L;

    public EnlargeConfig conf;
    public String fid;
    public String status;
    public String time;
    // 放大后的文件地址
    public String enlargeUrl;
    public boolean isChecked;


    public void create(JsonArray jsonArray){
        try {
            JsonElement jsonObject = jsonArray.get(0);
            if(jsonObject != null){
                conf = GsonUtil.getGson().fromJson(jsonObject, EnlargeConfig.class);
            }
            enlargeUrl = jsonArray.get(1).getAsString();
            time = jsonArray.get(2).getAsString();
            status = jsonArray.get(3).getAsString();
            fid = jsonArray.get(4).getAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
