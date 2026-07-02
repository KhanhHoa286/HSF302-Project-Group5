package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ApplicantDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.EducationDto;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.ExperienceDto;
import vn.edu.fpt.hsf302_group5.entity.Application;
import vn.edu.fpt.hsf302_group5.entity.CandidateSkill;
import vn.edu.fpt.hsf302_group5.entity.Education;
import vn.edu.fpt.hsf302_group5.entity.Experience;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {

    @Mapping(target = "fullName", expression = "java(application.getCandidateFullName())")
    @Mapping(target = "email", expression = "java(application.getCandidateEmail())")
    @Mapping(target = "phone", expression = "java(application.getCandidatePhone())")
    ApplicantResponse toApplicantResponse(Application application);

    @Mapping(target = "fullName", expression = "java(application.getCandidateFullName())")
    @Mapping(target = "email", expression = "java(application.getCandidateEmail())")
    @Mapping(target = "phone", expression = "java(application.getCandidatePhone())")
    @Mapping(target = "avatarUrl", expression = "java(application.getCandidateAvatarUrl())")
    @Mapping(target = "jobId", expression = "java(application.getJobId())")
    @Mapping(target = "jobTitle", expression = "java(application.getJobTitle())")
    @Mapping(target = "dateOfBirth", expression = "java(application.getCandidateDateOfBirth())")
    @Mapping(target = "addressDetail", expression = "java(application.getCandidateAddressDetail())")
    @Mapping(target = "provinceName", expression = "java(application.getCandidateProvinceName())")
    @Mapping(target = "summary", expression = "java(application.getCandidateSummary())")
    @Mapping(target = "skills", expression = "java(application.getCandidateSkills())")
    @Mapping(target = "educations", expression = "java(application.getCandidateEducations() != null ? application.getCandidateEducations().stream().map(this::toEducationDto).toList() : java.util.List.of())")
    @Mapping(target = "experiences", expression = "java(application.getCandidateExperiences() != null ? application.getCandidateExperiences().stream().map(this::toExperienceDto).toList() : java.util.List.of())")
    @Mapping(target = "cvName", expression = "java(application.getCvName())")
    @Mapping(target = "cvUrl", expression = "java(application.getCvUrl())")
    @Mapping(target = "gender", expression = "java(application.getCandidateGender())")
    ApplicantDetailResponse toApplicantDetailResponse(Application application);

    EducationDto toEducationDto(Education education);

    ExperienceDto toExperienceDto(Experience experience);
}
