package com.bill.zhihu.api.bean.common;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by bill_lv on 2016/3/8.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Author implements Parcelable{

    @JsonProperty("headline")
    public String headline;
    @JsonProperty("avatar_url")
    public String avatarUrl;
    @JsonProperty("name")
    public String name;
    @JsonProperty("url")
    public String authorUrl;
    // male 1, female 0
    @JsonProperty("gender")
    public String gender;
    @JsonProperty("type")
    public String type;
    @JsonProperty("id")
    public String authorId;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.headline);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.name);
        dest.writeString(this.authorUrl);
        dest.writeString(this.gender);
        dest.writeString(this.type);
        dest.writeString(this.authorId);
    }

    public Author() {
    }

    protected Author(Parcel in) {
        this.headline = in.readString();
        this.avatarUrl = in.readString();
        this.name = in.readString();
        this.authorUrl = in.readString();
        this.gender = in.readString();
        this.type = in.readString();
        this.authorId = in.readString();
    }

    public static final Creator<Author> CREATOR = new Creator<Author>() {
        public Author createFromParcel(Parcel source) {
            return new Author(source);
        }

        public Author[] newArray(int size) {
            return new Author[size];
        }
    };
}
