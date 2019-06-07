package com.tapatuniforms.pos.helper;

public interface APIStatic {
    String baseURL = "http://192.168.1.107:8000/api/";

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
        String endPoint = "user/";
        String loginOTPURL = baseURL + endPoint + "otp/";
        String accountURL = baseURL + endPoint + "account/";
        String otpRegLoginURL = baseURL + endPoint + "otpreglogin/";

        String keyDestination = "destination";
        String keyUsername = "username";
        String keyMobile = "mobile";
        String keyEmail = "email";
        String keyIsLogin = "is_login";
        String keyVerifyOTP = "verify_otp";
        String keyToken = "token";
    }

    interface Outlet {
        String endPoint = "outlets/";
        String productUrl = baseURL + endPoint + "products/";

        String keyProduct = "product";
        String keyCategory = "category";
        String keyPrice = "price";
        String keyOutlet = "outlet";
        String keyImage = "image";
        String keySku = "sku";
        String keySize = "size";
    }

    interface Category {
        String endPoint = "categories/";
        String categoryUrl = baseURL + endPoint;

    }
}
