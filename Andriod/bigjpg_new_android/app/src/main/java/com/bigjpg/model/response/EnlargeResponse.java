package com.bigjpg.model.response;

/**
 * {"status":"ok","info":"3a819503a25c40f2b0652b1fa749f683","time":2}
 */
public class EnlargeResponse extends HttpResponse {
    private static final long serialVersionUID = 1L;

    // fid
    private String info;

    // 预估时间min
    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
