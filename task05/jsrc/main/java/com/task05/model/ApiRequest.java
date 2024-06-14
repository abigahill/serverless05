package com.task05.model;

import java.util.Map;

public class ApiRequest {
    private int principalId;
    private Map<String, String> content;

    public int getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(int principalId) {
        this.principalId = principalId;
    }

    public Map<String, String> getContent() {
        return content;
    }

    public void setContent(Map<String, String> content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Request{ principalId=" + principalId +
                ", content=" + content + " }";
    }
}