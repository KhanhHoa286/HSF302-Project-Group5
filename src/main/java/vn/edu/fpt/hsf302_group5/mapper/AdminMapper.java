package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.fpt.hsf302_group5.dto.admin.AdminJobDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.CompanyIndustry;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(source = "recruiter.company.companyName", target = "companyName", defaultValue = "N/A")
    @Mapping(source = "recruiter.company.logoUrl", target = "logoUrl", defaultValue = "")
    @Mapping(source = "province.provinceName", target = "provinceName", defaultValue = "")
    AdminJobDetailResponse toAdminJobDetailResponse(JobPost jobPost);

    @Mapping(source = "recruiter.company.companyName", target = "companyName", defaultValue = "N/A")
    JobPostDashboardResponse toJobPostDashboardResponse(JobPost jobPost);

    @Mapping(target = "email", source = "recruiter.user.email", defaultValue = "Không có")
    @Mapping(target = "website", source = "website", defaultValue = "")
    CompanyDashboardResponse toCompanyDashboardResponse(Company company);

    @Mapping(target = "recruiterId", source = "recruiter.recruiterId")
    @Mapping(target = "recruiterName", source = "recruiter.user.fullName", defaultValue = "Không có")
    @Mapping(target = "recruiterEmail", source = "recruiter.user.email", defaultValue = "Không có")
    @Mapping(target = "recruiterPhone", source = "recruiter.user.phone", defaultValue = "Không có")
    @Mapping(target = "recruiterAvatarUrl", source = "recruiter.user.avatarUrl", defaultValue = "")
    @Mapping(target = "provinceName", source = "province.provinceName", defaultValue = "Chưa cập nhật")
    @Mapping(target = "administrativeUnitName", source = "administrativeUnit.unitName", defaultValue = "Chưa cập nhật")
    @Mapping(target = "industries", source = "companyIndustries")
    CompanyDetailResponse toCompanyDetailResponse(Company company);

    default List<String> mapIndustries(Set<CompanyIndustry> companyIndustries) {
        if (companyIndustries == null) {
            return Collections.emptyList();
        }
        return companyIndustries.stream()
                .map(ci -> ci.getIndustry().getIndustryName())
                .collect(Collectors.toList());
    }
}
