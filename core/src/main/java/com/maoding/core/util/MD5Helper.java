package com.maoding.core.util;

import java.security.MessageDigest;

/**
 * Created by Xx on 2016/7/11.
 */
public class MD5Helper {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static String getMD5For32(String sourceString) throws Exception {
        String resultString = null;
        if (sourceString != null && !sourceString.isEmpty() && !"".equals(sourceString.trim())) {
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(sourceString.getBytes());
            byte messageDigest[] = digest.digest();
            resultString = toHexString(messageDigest);
        }
        return resultString;
    }

    public static String getMD5For16(String sourceString) throws Exception {
        String resultString = null;
        if (sourceString != null && !sourceString.isEmpty()) {
            resultString = getMD5For32(sourceString);
            resultString = resultString.substring(8, 24);
        }
        return resultString;
    }
}
