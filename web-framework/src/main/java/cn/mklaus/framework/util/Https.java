package cn.mklaus.framework.util;

import org.nutz.http.Http;
import org.nutz.lang.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author Mklaus
 * Created on 2018-01-02 下午2:05
 */
public class Https {

    public static String extraParameter(HttpServletRequest req, String split) {
        if (req.getParameterMap() == null || req.getParameterMap().isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        req.getParameterMap().entrySet().forEach(
                e ->
                        sb.append(e.getKey())
                                .append(": ")
                                .append(e.getValue().length > 1 ? Arrays.toString(e.getValue()) : e.getValue()[0])
                                .append(split)
        );
        return sb.toString();
    }

    public static String extraCookies(HttpServletRequest req, String split) {
        if (req.getCookies() == null || req.getCookies().length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Arrays.stream(req.getCookies()).forEach(
                cookie ->
                        sb.append(cookie.getName()).append(": ")
                                .append(cookie.getValue()).append(split)
        );
        return sb.toString();
    }

    public static String extraHeader(HttpServletRequest req, String split) {
        if (req.getHeaderNames() == null || !req.getHeaderNames().hasMoreElements()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String s = headerNames.nextElement();
            sb.append(s).append(": ").append(req.getHeader(s)).append(split);
        }
        return sb.toString();
    }

    public static boolean acceptHtml(final HttpServletRequest req) {
        String xmlHttpRequest = req.getHeader("x-requested-with");
        boolean ajax = Strings.isNotBlank(xmlHttpRequest);
        String accept = req.getHeader("accept");
        return !ajax && !Objects.isNull(accept) && accept.contains("text/html");
    }

    public static String getCookie(final HttpServletRequest req, final String name) {
        if (req.getCookies() == null || req.getCookies().length == 0) {
            return null;
        }
        for (Cookie cookie : req.getCookies()) {
            if (cookie.getName().equals(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void setCookie(HttpServletResponse resp, String name, String value, int time) {
        setCookie(resp, name, value, time, false);
    }

    public static void setCookie(HttpServletResponse resp, String name, String value, int time, boolean httpOnly) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(httpOnly);
        cookie.setPath("/");
        cookie.setMaxAge(time);
        resp.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse resp, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static void response(String message, HttpServletResponse resp) {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out;
        try {
            out = resp.getWriter();
            out.print(message);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void responseImage(File image, HttpServletResponse resp) throws IOException {
        responseFile(image, "", "image/png", resp);
    }

    public static void responseFile(File file, String filename, String contentType, HttpServletResponse resp) throws IOException {
        if (Strings.isNotBlank(filename)) {
            resp.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(filename, "UTF-8"));
        }
        resp.setContentType(contentType);
        try (FileInputStream in = new FileInputStream(file);OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[4096];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
        }
    }

    public static File download(String url) throws IOException {
        File temp = File.createTempFile("temp", "");
        try (InputStream is = Http.get(url).getStream(); OutputStream os = new FileOutputStream(temp)){
            byte[] buffer = new byte[8192];
            int l;
            while((l = is.read(buffer)) != -1) {
                os.write(buffer, 0, l);
            }
        }
        return temp;
    }

    public boolean isWechat(HttpServletRequest req) {
        String userAgent = req.getHeader("user-agent");
        return Strings.isNotBlank(userAgent) && userAgent.toLowerCase().contains("micromessenger");
    }

    public HttpStatus getStatus(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE);
        if (statusCode == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return HttpStatus.valueOf(statusCode);
    }

}
