package az.xazar.msvacation.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
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
    private String result;
    private boolean isDeleted;
    private Long fileId;
    private String fileName;

    //@SuppressWarnings("java:S1948")
    @JsonIgnore
    private MultipartFile file;
}
