package vn.edu.fpt.hsf302_group5.service.company;

import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;

public interface CompanyService {
    CompanyProfileResponse getCompanyProfile(String userEmail);
    void updateCompanyProfile(String userEmail, CompanyProfileRequest request);
}
