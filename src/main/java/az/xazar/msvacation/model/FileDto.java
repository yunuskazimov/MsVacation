package az.xazar.msvacation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDto {
    //  private static final long serialVersionUID = 232836038145089522L;
    private Long id;
    private Long fileId;
    private String fileName;
    private Long userId;
    private String type;
    private boolean isDeleted;

    @SuppressWarnings("java:S1948")
    private MultipartFile file;

}
