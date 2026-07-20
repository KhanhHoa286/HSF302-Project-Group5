package vn.edu.fpt.hsf302_group5.service.company;

import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;

public interface CompanyService {
    CompanyProfileResponse getCompanyProfile(String userEmail);
    void updateCompanyProfile(String userEmail, CompanyProfileRequest request);
    CompanyProfileResponse getCompanyProfileById(Integer companyId);
    CompanyDetailResponse getCompanyDetailById(Integer companyId);
}
