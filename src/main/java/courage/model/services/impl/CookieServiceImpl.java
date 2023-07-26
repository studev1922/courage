package courage.model.services.impl;

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
        if(req.getCookies() != null && name!=null)
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
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        res.addCookie(cookie);
    }

    @Override
    public Cookie createCookie(String name, String value, int age) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(age);
        res.addCookie(c);
        return c;
    }

}
