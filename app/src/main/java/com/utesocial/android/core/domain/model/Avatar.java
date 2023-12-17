package com.utesocial.android.core.domain.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Avatar {
    @SerializedName("_id")
    public final String id;
    public final String url;

    public Avatar(String id, String url) {
        this.id = id;
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return "Avatar{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
