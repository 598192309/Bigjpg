package com.bigjpg.model.response;

/**
 * PaypalResponse
 * @author Momo
 * @date 2019-04-22 11:10
 */
public class PaypalResponse extends HttpResponse {

    private static final long serialVersionUID = 1L;

    // paypal url
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
