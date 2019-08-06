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
        String discountUrl = baseURL + endPoint + "discount/";
    }

    interface School {
        String endPoint = "school/";
        String addDetailsUrl = baseURL + endPoint + "student/";
    }

    interface StockOrder {
        String endPoint = "stockOrder/";
        String indentRequest = baseURL + endPoint + "indentRequest/";
        String indentRequestDetails = indentRequest + "indentRequestDetails/";
        String getIndentUrl = baseURL + endPoint;
        String getBoxUrl = baseURL + endPoint + "box/";
        String getBoxItem = getBoxUrl + "boxItem/";
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
        String verifyOtp = "verify_otp";
        String token = "token";
        String product = "product";
        String outletSubproductSet = "outletsubproduct_set";
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
        String numberOfBoxes = "num_of_boxes";
        String shippingFrom = "shipping_from";
        String boxCode = "box_code";
        String totalItems = "total_item";
        String indent = "indent";
        String indentRequest = "indent_request";
        String numberOfMaleItems = "male_items";
        String numberOfFemaleItems = "female_items";
        String numberOfItems = "num_of_item";
        String numberOfScannedItems = "item_scanned";
        String box = "box";
        String productQuantity = "product_quantity";
        String discountType = "discount_type";
        String value = "value";
        String warehouseStock = "warehouse_stock";
        String displayStock = "display_stock";
    }

    interface Constants {
        String MALE = "M";
        String FEMALE = "F";
        String PERCENTAGE = "P";
        String AMOUNT = "A";
        String OTHER = "O";
    }
}
