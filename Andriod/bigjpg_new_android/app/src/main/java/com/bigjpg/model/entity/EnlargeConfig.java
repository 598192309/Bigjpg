package com.bigjpg.model.entity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *         'x2': 放大倍数 1 两倍, 2 四倍, 3 八倍, 4 十六倍,
 *         'style': art 卡通图片, photo 照片,
 *         'noise': 噪点 -1 无, 0 低, 1 中, 2 高, 3 最高,
 *         'file_name': 图片名,
 *         'files_size': 图片字节大小,
 *         'file_height': 图片高度,
 *         'file_width': 图片宽度
 */
public class EnlargeConfig implements IEntity {

    private static final long serialVersionUID = 1L;

    public int x2;

    public String style;

    public int noise;

    public String file_name;

    public float files_size;

    public int file_height;

    public int file_width;

    public String input;

    public String file_path;

    public String fid;

    //enlarge status
    public String status = EnlargeStatus.NEW;
    public String tid;

    //may be "null"
    public String image_url;
    public int progress;
    public boolean isOverLimit;

    public String getEnlargeParam(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("x2", x2);
            jsonObject.put("style", style);
            jsonObject.put("noise", noise);
            jsonObject.put("file_name", file_name);
            jsonObject.put("files_size", files_size);
            jsonObject.put("file_height", file_height);
            jsonObject.put("file_width", file_width);
            jsonObject.put("input", input);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    /**
     * 批量放大时复制参数
     * @param target
     * @param source
     */
    public static void copyEnlargeConfigParam(EnlargeConfig target, EnlargeConfig source){
        target.x2 = source.x2;
        target.style = source.style;
        target.noise = source.noise;
    }

    public static class Style{
        public static final String ART = "art";
        public static final String PHOTO = "photo";
    }

    public static class X2{
        public static final int L2 = 1;
        public static final int L4 = 2;
        public static final int L8 = 3;
        public static final int L16 = 4;
    }

    public static class Noise{
        public static final int NONE = -1;
        public static final int LOW = 0;
        public static final int MEDIUM = 1;
        public static final int HIGH = 2;
        public static final int HIGHEST = 3;
    }
}
