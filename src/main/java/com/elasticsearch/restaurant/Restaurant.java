package com.elasticsearch.restaurant;

import lombok.Builder;
import lombok.Getter;

@Getter
public class Restaurant {
    private String name;
    private String category1;
    private String category2;
    private String category3;
    private String state;
    private String city;
    private String description;

    @Builder
    public Restaurant(String name, String category1, String category2, String category3, String state, String city, String description) {
        this.name = name;
        this.category1 = category1;
        this.category2 = category2;
        this.category3 = category3;
        this.state = state;
        this.city = city;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", category1='" + category1 + '\'' +
                ", category2='" + category2 + '\'' +
                ", category3='" + category3 + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
