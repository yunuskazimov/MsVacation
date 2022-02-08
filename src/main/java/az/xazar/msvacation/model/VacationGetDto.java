package az.xazar.msvacation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationGetDto {
    private Long id;
    private Long userId;
    private String userName;
    private String startDate;
    private String endDate;
    private Result result;
    private boolean deleted;
}
