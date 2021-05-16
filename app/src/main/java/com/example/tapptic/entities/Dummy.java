package com.example.tapptic.entities;

import com.google.gson.annotations.SerializedName;

public class Dummy {
    @SerializedName("name")
    private final String name;
    @SerializedName("image")
    private final String image;
    @SerializedName("text")
    private final String text;

    public Dummy(String name, String image, String text) {
        this.name = name;
        this.image = image;
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Dummy dummy = (Dummy) o;

        if (name != null ? !name.equals(dummy.name) : dummy.name != null) return false;
        if (image != null ? !image.equals(dummy.image) : dummy.image != null) return false;
        return text != null ? text.equals(dummy.text) : dummy.text == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (image != null ? image.hashCode() : 0);
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
