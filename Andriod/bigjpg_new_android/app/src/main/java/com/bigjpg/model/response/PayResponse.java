package com.bigjpg.model.response;

/**
 * PayResponse
 * @author Momo
 * @date 2019-04-22 11:10
 */
public class PayResponse extends HttpResponse {

    private static final long serialVersionUID = 1L;

    // qr里面的内容有赞支付url
    private String qr_url;

    // base64 编码的youzan支付链接QR
    private String qr;

    public String getQr_url() {
        return qr_url;
    }

    public void setQr_url(String qr_url) {
        this.qr_url = qr_url;
    }

    public String getQr() {
        return qr;
    }

    public void setQr(String qr) {
        this.qr = qr;
    }
}
