package com.anytec.userservice.utils;

import com.google.common.base.Throwables;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class HashUtils {

    public static final HashFunction FUNCTION = Hashing.md5();

    public static final String SALT = "mooc.com";

    private static final HashFunction MURMUR_FUNC = Hashing.murmur3_128();

    public static String encryPassword(String password){
        HashCode hashCode = FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }

    public static String hashString(String input){
        HashCode code = null;
        try {
            code = MURMUR_FUNC.hashBytes(input.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            Throwables.propagate(e);
        }
        return code.toString();
    }

}
