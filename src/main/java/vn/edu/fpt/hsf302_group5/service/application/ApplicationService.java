package vn.edu.fpt.hsf302_group5.service.application;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;

public interface ApplicationService {
    Page<ApplicantResponse> getApplicantsByFilter(Integer jobId, String searchKeyword, String status, int page);
}
