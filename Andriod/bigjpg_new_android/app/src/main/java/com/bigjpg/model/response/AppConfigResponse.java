package com.bigjpg.model.response;

import com.bigjpg.model.entity.OssConfig;

public class AppConfigResponse extends HttpResponse {

    private static final long serialVersionUID = 1L;

    private String newest_app;

    private long version;

    private OssConfig app_oss_conf;

    public String getNewest_app() {
        return newest_app;
    }

    public void setNewest_app(String newest_app) {
        this.newest_app = newest_app;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public OssConfig getApp_oss_conf() {
        return app_oss_conf;
    }

    public void setApp_oss_conf(OssConfig app_oss_conf) {
        this.app_oss_conf = app_oss_conf;
    }
}
