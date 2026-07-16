package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileResponse;
import vn.edu.fpt.hsf302_group5.dto.candidate.EducationDto;
import vn.edu.fpt.hsf302_group5.dto.candidate.ExperienceDto;
import vn.edu.fpt.hsf302_group5.dto.candidate.SkillDto;
import vn.edu.fpt.hsf302_group5.entity.CandidateProfile;
import vn.edu.fpt.hsf302_group5.entity.CandidateSkill;
import vn.edu.fpt.hsf302_group5.entity.Education;
import vn.edu.fpt.hsf302_group5.entity.Experience;
import vn.edu.fpt.hsf302_group5.entity.Skill;

@Mapper(componentModel = "spring")
public interface CandidateMapper {

    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.phone", target = "phone")
    @Mapping(source = "user.gender", target = "gender")
    @Mapping(source = "user.avatarUrl", target = "avatarUrl")
    @Mapping(source = "candidateId", target = "candidateId")
    CandidateProfileResponse toResponse(CandidateProfile profile);

    EducationDto toEducationDto(Education education);

    ExperienceDto toExperienceDto(Experience experience);

    @Mapping(source = "skill.skillId", target = "skillId")
    @Mapping(source = "skill.skillName", target = "skillName")
    SkillDto toSkillDto(CandidateSkill candidateSkill);

    @Mapping(source = "skillId", target = "skillId")
    @Mapping(source = "skillName", target = "skillName")
    SkillDto toSkillDtoFromEntity(Skill skill);

    CandidateProfileRequest toRequest(CandidateProfileResponse response);
}
