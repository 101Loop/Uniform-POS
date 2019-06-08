package com.tapatuniforms.pos.helper;

public interface APIStatic {
    String baseURL = "http://192.168.1.107:8000/api/";

    String dateTimeFormat = "yyyy-MM-dd'T'HH:mm:ss";
    String onlyDateFormat = "yyyyMMdd";

    interface User {
        String endPoint = "user/";
        String loginOTPURL = baseURL + endPoint + "otp/";
        String accountURL = baseURL + endPoint + "account/";
        String otpRegLoginURL = baseURL + endPoint + "otpreglogin/";
    }

    interface Outlet {
        String endPoint = "outlets/";
        String productUrl = baseURL + endPoint + "products/";
    }

    interface Category {
        String endPoint = "categories/";
        String categoryUrl = baseURL + endPoint;
    }

    interface Order {
        String endPoint = "order/";
        String orderUrl = baseURL + endPoint;
        String subOrderUrl = baseURL + endPoint + "subOrder/";
        String transactionUrl = baseURL + endPoint + "transaction/";
    }

    interface Key {
        String id = "id";
        String name = "name";
        String count = "count";
        String next = "next";
        String result = "results";
        String phone = "phone";
        String previous = "previous";
        String page = "page";
        String last = "last";
        String success = "success";
        String message = "message";
        String detail = "detail";
        String destination = "destination";
        String username = "username";
        String mobile = "mobile";
        String email = "email";
        String isLogin = "is_login";
        String verifyOtp= "verify_otp";
        String token = "token";
        String product = "product";
        String category = "category";
        String price = "price";
        String outlet = "outlet";
        String image = "image";
        String sku = "sku";
        String size = "size";
        String qunatity = "quantity";
        String order = "order";
        String amount = "amount";
        String mode = "mode";
        String total = "total";
        String discount = "discount";
    }
}
