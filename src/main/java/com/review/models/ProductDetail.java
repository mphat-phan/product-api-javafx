package com.review.models;

import java.io.Serializable;

public class ProductDetail implements Serializable {
    private String[] imagesUrl;

    private String description;

    private Integer rating_average;
    private Integer review_count;
    public void setImagesUrl(String[] imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public String getDescription() {
        return description;
    }
    public String[] getImagesUrl() {
        return imagesUrl;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getRating_average() {
        return rating_average;
    }

    public void setRating_average(Integer rating_average) {
        this.rating_average = rating_average;
    }

    public Integer getReview_count() {
        return review_count;
    }

    public void setReview_count(Integer review_count) {
        this.review_count = review_count;
    }
}