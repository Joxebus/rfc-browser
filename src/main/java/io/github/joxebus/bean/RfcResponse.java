package io.github.joxebus.bean;

import java.util.List;

public class RfcResponse {
    private String filename;
    private List<RfcTextLine> content;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public List<RfcTextLine> getContent() {
        return content;
    }

    public void setContent(List<RfcTextLine> content) {
        this.content = content;
    }
}
