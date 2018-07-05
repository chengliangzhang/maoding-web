package com.maoding.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Xx on 2016/7/13.
 */
public class HTTPHelper {

    public static boolean isAjaxRequest(HttpServletRequest httpServletRequest) {
        return ("XMLHttpRequest").equalsIgnoreCase(httpServletRequest.getHeader("X-Requested-With"));
    }

    public static void returnJson(ServletResponse response, Object jsonObject) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(jsonObject);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.append(jsonString);
        } finally {
            if (out != null)
                out.close();
        }
    }
}
