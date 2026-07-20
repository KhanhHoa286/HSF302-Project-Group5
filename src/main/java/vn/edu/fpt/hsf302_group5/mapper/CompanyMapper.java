package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

    @Mapping(target = "companyId", expression = "java(company.getCompanyId())")
    @Mapping(target = "companyName", expression = "java(company.getCompanyName())")
    @Mapping(target = "logoUrl", expression = "java(company.getLogoUrl())")
    @Mapping(target = "website", expression = "java(company.getWebsite())")
    @Mapping(target = "description", expression = "java(company.getDescription())")
    @Mapping(target = "addressDetail", expression = "java(company.getAddressDetail())")
    @Mapping(target = "email", expression = "java(company.getRecruiterUserEmail())")
    @Mapping(target = "phone", expression = "java(company.getRecruiterUserPhone())")
    CompanyProfileResponse toResponse(Company company);

    @Mapping(target = "companyId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "recruiter", ignore = true)
    @Mapping(target = "companyIndustries", ignore = true)
    @Mapping(target = "provinceId", ignore = true)
    @Mapping(target = "province", ignore = true)
    @Mapping(target = "administrativeUnitId", ignore = true)
    @Mapping(target = "administrativeUnit", ignore = true)
    void updateEntityFromRequest(CompanyProfileRequest request, @MappingTarget Company company);
}
