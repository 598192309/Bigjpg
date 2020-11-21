package com.bigjpg.model.entity;

/**
 * 'username': 用户名,
 * 'version': 用户等级(免费版 None, 基础版basic, 标准版std, 高级版pro),
 * 'expire': 过期时间,
 * 'is_expire': 是否过期或者免费用户
 */
public class User implements IEntity {

    public String nickname;

    public String username;

    public String version;

    public String expire;

    public boolean is_expire;

    public int used;

    public static class  Version{
        public static final String NONE = "none";
        public static final String BASIC = "basic";
        public static final String STANDARD = "std";
        public static final String PRO = "pro";
    }

}
