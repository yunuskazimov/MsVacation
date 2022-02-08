package az.xazar.msvacation.client;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
@Slf4j
public class PermissionClientRest {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public PermissionClientRest(RestTemplate restTemplate
            , @Value("${client.permission.int.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public boolean getRoleByUserId(Long userId, String userRole) {
        UserRoleForm form = new UserRoleForm();
        form.setUserId(userId);
        form.setUserRole(userRole);
        String url = String.format("%s", apiUrl);
        log.info("get role by user Id: {}, Role: {}", userId, userRole);

        return Boolean.TRUE.equals(restTemplate.postForObject(url, form, Boolean.class));
    }
}

@Data
class UserRoleForm {
    private Long userId;
    private String userRole;
}
