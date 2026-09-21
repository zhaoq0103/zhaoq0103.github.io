package com.ai.x.exception;

public record ApiErrorResponse (ApiError error, String data, String message){
}
