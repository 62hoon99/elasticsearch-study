package com.elasticsearch.search.domain;

public class NoriAnalyzerDocument {
    private String description;

    public NoriAnalyzerDocument(String description) {
        this.description = description;
    }

    public NoriAnalyzerDocument() {
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
