package courage.model.services;

import java.util.Map;

import jakarta.servlet.http.Cookie;

public interface CookieService {
    Cookie getCookie(String name);

    void setCookie(String name, String value, int age);

    void setCookie(String name, String value, int age, Map<String, String> attrs);

    void remove(String name);
}