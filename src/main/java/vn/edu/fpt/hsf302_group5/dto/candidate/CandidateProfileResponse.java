package vn.edu.fpt.hsf302_group5.dto.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileResponse {
    private Integer candidateId;
    private String email;
    private String fullName;
    private String phone;
    private Gender gender;
    private String avatarUrl;
    private LocalDate dateOfBirth;
    private String addressDetail;
    private String summary;
    private List<EducationDto> educations;
    private List<ExperienceDto> experiences;
    private List<SkillDto> skills;
}
