package com.bigjpg.model.response;

/**
 * {"3a819503a25c40f2b0652b1fa749f683":["error",null]}
 */
public class EnlargeProcessResponse extends HttpResponse implements IOriginResponse{

    private static final long serialVersionUID = 1L;

    private String data;

    @Override
    public void setOriginData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
