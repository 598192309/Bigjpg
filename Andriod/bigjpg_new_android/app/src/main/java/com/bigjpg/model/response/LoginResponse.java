package com.bigjpg.model.response;

/**
 *    - {'status': 'error'} 参数错误
 *     - {'status': 'not_exist'} 用户不存在
 *     - {'status': 'password_error'} 密码错误
 *     - {
 *           'status': 'ok',
 *           'username': 用户名,
 *           'is_expire': 是否过期或者免费版用户
 *        }, 同时返回的还有Http Header里面的 Set-Cookie，需要把这个cookie存起来,以后的请求都要带上
 */
public class LoginResponse extends HttpResponse {

    private static final long serialVersionUID = 1L;

    private String username;

    private boolean is_expire;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isIs_expire() {
        return is_expire;
    }

    public void setIs_expire(boolean is_expire) {
        this.is_expire = is_expire;
    }
}
