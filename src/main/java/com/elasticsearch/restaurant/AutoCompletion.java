package com.elasticsearch.restaurant;

import lombok.Getter;

@Getter
public class AutoCompletion {
    private String suggest;

    public AutoCompletion(String suggest) {
        this.suggest = suggest;
    }

    public AutoCompletion() {
    }
}
