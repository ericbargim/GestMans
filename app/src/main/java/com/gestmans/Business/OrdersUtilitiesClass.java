package com.gestmans.Business;

import java.util.Arrays;
import java.util.List;

public class OrdersUtilitiesClass {
    public static List<String> stringToListCapitalize(String data) {
        // Transform String to List and capitalize first letter
        String[] dishTypes = data.split("-");
        List<String> listDishTypes = Arrays.asList(dishTypes);
        return listDishTypes = HelperClass.capitalizeLetters(listDishTypes);
    }
}
