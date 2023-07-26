package courage.model.util;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.shaded.gson.Gson;
import com.nimbusds.jose.shaded.gson.reflect.TypeToken;

import courage.model.authHandle.Authorization.R;
import courage.model.entities.UAccount;

class u {
    final static java.util.Random r = new java.util.Random();
}

public interface Utils {

    static UserDetails from(UAccount account) {
        String unique = account.getUsername();
        String password = account.getPassword();
        Set<Integer> roleIds = account.getRoles();
        String[] roles = new String[roleIds.size()];
        R[] rs = R.values();
        int i = 0;

        if (unique == null)
            unique = account.getEmail();
        if (password == null)
            password = String.valueOf(System.currentTimeMillis());
        for (Integer ordinal : roleIds)
            roles[i++] = rs[ordinal].name();

        return User.withUsername(unique)
                .password(password)
                .roles(roles).build();
    }

    static String build(Object... objs) {
        StringBuilder builder = new StringBuilder();
        for (Object obj : objs)
            builder.append(obj);
        return builder.toString();
    }

    static String jsonMessage(String key, String message) {
        return new StringBuilder("{\"").append(key).append("\":\"").append(message).append("\"}").toString();
    }

    static String jsonMessage(Map<String, Object> map) {
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.toJson(map, type);
    }

    static String[] merger(Object[]... data) {
        // get the total length of the merged array
        int length = 0;
        for (Object[] array : data)
            length += array.length;

        // create a new array with the same length
        String[] merged = new String[length];

        int index = 0; // copy the elements from each array to the merged array
        // use System.arraycopy() instead of for loop
        for (Object[] array : data) {
            System.arraycopy(array, 0, merged, index, array.length);
            index += array.length;
        }
        // return the merged array
        return merged;
    }

    static String generalCode(String firstText, int length) {
        StringBuilder b = new StringBuilder(firstText == null ? "" : firstText);
        int i; // @formatter:off
        while(b.length()<length)
            if(((i=u.r.nextInt(122))>47&&i<58)||(i>64&&i<91)||(i>96&&i<123))
                b.append((char)i);
        return b.length()>length?b.substring(0,length):b.toString();
    }// @formatter:on
}
