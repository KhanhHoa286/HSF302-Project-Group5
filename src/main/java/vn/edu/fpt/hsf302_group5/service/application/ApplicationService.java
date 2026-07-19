package vn.edu.fpt.hsf302_group5.service.application;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.Application;

public interface ApplicationService {
    Page<ApplicantResponse> getApplicantsByFilter(Integer jobId, String searchKeyword, String status, int page);
    ApplicantDetailResponse getApplicantDetail(Integer applicationId);
    void updateApplicationStatus(Integer applicationId, String status);

    boolean hasApplied(Integer candidateId, Integer jobId);
    void applyForJob(Integer candidateId, Integer jobId, Integer cvId, String coverLetter);
    Page<Application> getApplicationsByCandidate(Integer candidateId, String status, int page, int size);
    Application getApplicationDetailForCandidate(Integer applicationId, Integer candidateId);
}

