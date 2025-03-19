package ru.samsung.raidespacex;

import com.google.gson.annotations.SerializedName;

public class DataFromBase {
    @SerializedName("id")
    int id;

    @SerializedName("playename")
    String name;

    @SerializedName("score")
    int score;

    @SerializedName("kills")
    int kills;

    @SerializedName("daterec")
    String daterec;
}
