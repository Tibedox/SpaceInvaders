package ru.samsung.spaceinvaders;

import com.google.gson.annotations.SerializedName;

public class DataFromDB {
    @SerializedName("id")
    int id;

    @SerializedName("name")
    String name;

    @SerializedName("score")
    int score;

    @SerializedName("kills")
    int kills;

    @SerializedName("rdatetime")
    String datetime;
}
