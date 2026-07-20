package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.CompanyIndustry;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Mapping(target = "recruiterId", source = "recruiter.recruiterId")
    @Mapping(target = "recruiterName", source = "recruiter.user.fullName", defaultValue = "Không có")
    @Mapping(target = "recruiterEmail", source = "recruiter.user.email", defaultValue = "Không có")
    @Mapping(target = "recruiterPhone", source = "recruiter.user.phone", defaultValue = "Không có")
    @Mapping(target = "recruiterAvatarUrl", source = "recruiter.user.avatarUrl", defaultValue = "")
    @Mapping(target = "provinceName", source = "province.provinceName", defaultValue = "Chưa cập nhật")
    @Mapping(target = "administrativeUnitName", source = "administrativeUnit.unitName", defaultValue = "Chưa cập nhật")
    @Mapping(target = "industries", source = "companyIndustries", qualifiedByName = "mapIndustries")
    CompanyDetailResponse toDetailResponse(Company company);
    @Named("mapIndustries")
    default List<String> mapIndustries(Set<CompanyIndustry> companyIndustries) {
        if (companyIndustries == null) {
            return Collections.emptyList();
        }
        return companyIndustries.stream()
                .map(ci -> ci.getIndustry().getIndustryName())
                .collect(Collectors.toList());
    }

}
