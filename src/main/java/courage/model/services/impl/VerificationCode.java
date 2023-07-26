package courage.model.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.DateTimeException;
import java.util.Date;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import courage.model.services.PropertyService;

@Service
public class VerificationCode implements PropertyService<String> {

    // @formatter:off
    private final File file;
    private final Properties PPS = new Properties();

    public VerificationCode() throws FileNotFoundException {
        this.file = ResourceUtils.getFile("classpath:properties/verification-code.properties");
    }

    @Override public File getFile() { return this.file; }
    @Override public Properties getProperties() { return this.PPS; }
    
    @Override
    public String get(String key) throws Exception {
        String code = this.PPS.getProperty(key);
        valueCode vc = new valueCode(code);
        if(vc.isAlive()) return vc.unique;
        throw new DateTimeException("Date expired at: "+new Date(vc.expired));
    }

    @Override
    public String pop(String key) throws Exception {
        String value = this.get(key);
        this.PPS.remove(key);
        return value;
    }

    /**
     * @param key to set value
     * @param values [unique, date expired];
     */
    @Override
    public Object put(String key, Object...values) throws Exception {
        String unique = values[0].toString();
        long expired = Long.parseLong(values[1].toString()); 
        String value = new valueCode(unique, expired).toString();
        return this.PPS.setProperty(key, value);
    }

    @Override
    public void move(String key) throws Exception {
        this.PPS.remove(key);
    }
    
    public void cleanExpired() {
        this.PPS.forEach((k,v) -> {
            String key = k.toString();
            try { this.get(key);}
            catch (Exception e) { this.PPS.remove(key); }
        });
    }


    private static class valueCode {
        protected final String unique;
        protected final long expired;

        valueCode(String value) throws Exception {
            if(value == null) throw new DateTimeException("Verification code is null!");
            String[] values = value.split(":");
            this.unique = values[0];
            this.expired = Long.parseLong(values[1]);
        };

        valueCode(String unique, long expired) {
            this.unique = unique;
            this.expired = expired;
        }

        boolean isAlive () {
            return System.currentTimeMillis() < expired;
        }

        @Override
        public String toString() {
            return new StringBuilder(unique).append(":").append(expired).toString();
        }
    } 
}
