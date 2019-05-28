package com.tapatuniforms.pos.helper;

public interface APIStatic {
    String baseURL = "";

    String keyID = "id";
    String keyName = "name";
    String keyCount = "count";
    String keyNext = "next";
    String keyResult = "results";
    String keyPhone = "phone";
    String keyPrevious = "previous";
    String keyPage = "page";
    String keyLast = "last";
    String keySuccess = "success";
    String keyMessage = "message";
    String keyDetail = "detail";

    String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    String onlyDateFormat = "yyyyMMdd";

    interface User {
        String userEndPoint = "users/";
        String loginOTPURL = APIStatic.baseURL + userEndPoint + "otp/";
        String accountURL = APIStatic.baseURL + userEndPoint + "account/";
        String otpRegLoginURL = APIStatic.baseURL + userEndPoint + "otpreglogin/";
        String keyDestination = "destination";
        String keyUsername = "username";
        String keyMobile = "mobile";
        String keyEmail = "email";
        String keyIsLogin = "is_login";
        String keyVerifyOTP = "verify_otp";
        String keyToken = "token";
    }
}
