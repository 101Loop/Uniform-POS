package com.tapatuniforms.pos.helper;

public interface APIStatic {
    String baseURL = "http://tapatapi.civilmachines.com/api/";

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

    interface School {
        String endPoint = "school/";
        String addDetailsUrl = baseURL + endPoint + "student/";
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
        String outletSubproductSet = "outlet_sub_product_set";
        String productType = "product_type";
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
        String color = "color";
        String colorCode = "color_code";
        String gender_type = "gender_type";
        String studentId = "student_id";
        String school = "school";
        String standard = "standard";
        String section = "section";
        String fatherName = "father_name";
        String gender = "gender";
    }

    interface Constants {
        String MALE = "M";
        String FEMALE = "F";
    }
}
