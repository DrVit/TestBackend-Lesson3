package ru.geekbrains.service;

public class Endpoints {
    public static final String getUserAccountInfo = "/account/{username}";
    public static final String postImage = "/image/{imageHash}";
    public static final String getImageUserAccountCount = "/account/{username}/images/count";
    public static final String getImageFavourite = "/image/{imageHash}/favorite";

}
