package com.example.urlshortner.util;

public class Base62Encoder {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE = 62;

    public String encode(Long num) {
        if (num < 0)        num = num * -1;
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62_CHARS.charAt((int)(num % BASE)));
            num /= BASE;
        }

        return sb.reverse().toString();
    }
}
