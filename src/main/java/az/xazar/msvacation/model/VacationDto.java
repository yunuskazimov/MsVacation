package az.xazar.msvacation.model;

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
    private String result;
    private boolean isDeleted;
    private Long fileId;
    private String fileName;

    //@SuppressWarnings("java:S1948")
    private MultipartFile file;
}
