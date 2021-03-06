package az.xazar.msvacation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationDto {
    private Long id;
    private Long userId;
    private String startDate;
    private String endDate;
    private Result result;
    private boolean deleted;
    private Long fileId;
    private String fileName;

    //@SuppressWarnings("java:S1948")
    @JsonIgnore
    private MultipartFile file;
}
