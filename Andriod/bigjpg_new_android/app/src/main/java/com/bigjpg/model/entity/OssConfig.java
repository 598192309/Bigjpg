package com.bigjpg.model.entity;

/**
 * {
 * "accesskeysecret":"4ifvcEGWMwobGvQWM3c4hefTMNJ7aI",
 * "endpoint":"https://oss-cn-shenzhen.aliyuncs.com",
 * "bucket":"bigjpg-upload",
 * "accesskeyid":"LTAIpUOrlri3zaVn"
 * }
 */
public class OssConfig implements IEntity{
    private static final long serialVersionUID = 1L;
    public String accesskeysecret;
    public String endpoint;
    public String bucket;
    public String accesskeyid;

    public String getOSSUrl(){
        String[] endpoints = endpoint.split("//");
        return endpoints[0] + "//" + bucket + "." + endpoints[1];
    }
}
