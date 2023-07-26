package courage.model.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.util.DefaultPropertiesPersister;

public interface PropertyService<T> {

    DefaultPropertiesPersister DPP = new DefaultPropertiesPersister();

    File getFile(); // file to read and write data

    Properties getProperties(); // file to read and write data

    T get(String key) throws Exception;
    T pop(String key) throws Exception;
    void move(String key) throws Exception;
    Object put(String key, Object...value) throws Exception;

    default void load() {
        try (FileInputStream fis = new FileInputStream(this.getFile())) {
            PropertyService.DPP.load(this.getProperties(), fis);
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.WARNING, PropertyService.class.getName(), ex);
        }
    }

    default public void store() {
        try (FileOutputStream fos = new FileOutputStream(this.getFile())) {
            PropertyService.DPP.store(this.getProperties(), fos, "WRITE FILE AT: " + new Date());
        } catch (IOException ex) {
            Logger.getGlobal().log(Level.WARNING, PropertyService.class.getName(), ex);
        }
    }
}
