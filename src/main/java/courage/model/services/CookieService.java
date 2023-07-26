package courage.model.services;

import jakarta.servlet.http.Cookie;

public interface CookieService {
    Cookie getCookie(String name);

    void setCookie(String name, String value, int age);

    Cookie createCookie(String name, String value, int age);

    void remove(String name);
}