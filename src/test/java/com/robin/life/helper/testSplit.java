package com.robin.life.helper;

public class testSplit {

    public static void main(String[] args) {
        String testData = "a:/adf/uploadImages/2020-04-10/1586510782380.jpg";
        String[] test = testData.split("uploadImages/");
        System.out.println(test[1]);
    }


}
