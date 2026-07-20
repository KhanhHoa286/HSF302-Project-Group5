package vn.edu.fpt.hsf302_group5.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

@Getter
@Setter
@NoArgsConstructor
public class JobApprovalRequest {
    private JobStatus status;
    private String adminComment;
}
