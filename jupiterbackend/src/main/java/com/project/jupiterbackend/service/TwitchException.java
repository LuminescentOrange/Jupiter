package com.project.jupiterbackend.service;


// throw this exception if there is something wrong when calling Twitch API.
public class TwitchException extends RuntimeException {
    public TwitchException(String errorMessage) {
        super(errorMessage);
    }
}