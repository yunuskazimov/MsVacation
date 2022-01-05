package az.xazar.msvacation.config;

import az.xazar.msvacation.mapper.VacationMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public VacationMapper btMapper() {
        return VacationMapper.INSTANCE;
    }

}
