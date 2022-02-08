package az.xazar.msvacation.client;


import az.xazar.msvacation.model.client.user.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class UserClientRest {
    private final RestTemplate restTemplate;
    private final String apiUrl;

    public UserClientRest(RestTemplate restTemplate
            , @Value("${client.users.int.url}") String apiUrl) {
        this.restTemplate = restTemplate;
        this.apiUrl = apiUrl;
    }

    public UserDto getById(Long id) {
        String url = String.format("%s/%d", apiUrl, id);
        return restTemplate.getForObject(url, UserDto.class);
    }
}
