package courage.model.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import courage.model.services.CookieService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieServiceImpl implements CookieService {

    @Autowired
    private HttpServletRequest req;
    @Autowired
    private HttpServletResponse res;

    @Override
    public Cookie getCookie(String name) {
        for (Cookie c : req.getCookies()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }

    @Override
    public void remove(String name) {
        Cookie c = this.getCookie(name);
        if (c != null)
            c.setMaxAge(0);
    }

    @Override
    public void setCookie(String name, String value, int age) {
        this.setCookie(name, value, age, null);
    }

    @Override
    public void setCookie(String name, String value, int age, Map<String, String> attrs) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(age);
        res.addCookie(c);
        if (attrs != null)
            attrs.forEach((k, v) -> {
                c.setAttribute(k, v);
            });
    }

}
