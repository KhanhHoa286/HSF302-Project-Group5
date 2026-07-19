package vn.edu.fpt.hsf302_group5.dto.recruiter.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationStatusHistoryDto {
    private Integer historyId;
    private ApplicationStatus oldStatus;
    private ApplicationStatus newStatus;
    private Integer changedBy;
    private String changerName;
    private LocalDateTime changedAt;
}
