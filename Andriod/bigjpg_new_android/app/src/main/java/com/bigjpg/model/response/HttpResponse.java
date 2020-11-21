package com.bigjpg.model.response;

import com.bigjpg.model.entity.IEntity;

/**
 * HttpResponse
 *
 * @author Momo
 * @date 2019-04-08 16:34
 */
public class HttpResponse implements IEntity {

    private static final long serialVersionUID = 1L;

    private String status;

    public boolean isOk() {
        return "ok".equals(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static boolean isResponseOk(HttpResponse response){
        return response != null && response.isOk();
    }


    /**
     * - {'status': 'error'} 参数错误
     * - {'status': 'not_exist'} 用户不存在
     * - {'status': 'password_error'} 密码错误
     * - {'status': 'parallel_limit'} 免费版同时只能放大两张
     * - {'status': 'month_limit'} 达到当月放大数量
     * - {'status': 'param_error'} 参数错误
     * - {'status': 'type_error'} 图片类型无法识别
     * - {'status': 'size_limit'} 图片尺寸超过限制
     */
    public static class Status {
        public static final String OK = "ok";
        public static final String ERROR = "error";
        public static final String NOT_EXIST = "not_exist";
        public static final String PASSWORD_ERROR = "password_error";
        public static final String NO_LOGIN = "no_login";
        public static final String PARALLEL_LIMIT = "parallel_limit";
        public static final String MONTH_LIMIT = "month_limit";
        public static final String PARAM_ERROR = "param_error";
        public static final String TYPE_ERROR = "type_error";
        public static final String SIZE_LIMIT = "size_limit";
    }

}
