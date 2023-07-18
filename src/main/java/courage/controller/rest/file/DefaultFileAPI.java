package courage.controller.rest.file;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import courage.controller.rest.AbstractFileAPI;

/**
 * Static file on server: http://localhost:8080/uploads/{path...}/{filename}
 * Static file on divide: [local
 * project...]/courage/src/main/webapp/uploads/{path...}/{filename}
 * Read byte[] as file: http://localhost:8080/{[@RequestMapping]
 * path...}/{filename}
 */
@RestController
@CrossOrigin("*") // file default api rest controller
@RequestMapping({ "/api/uploads/default", "/api/uploads/unknown" })
public class DefaultFileAPI extends AbstractFileAPI {
    DefaultFileAPI() {
        super("default");
    }
}