package vn.edu.fpt.hsf302_group5.service.candidate;

import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileResponse;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.EducationRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.ExperienceRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.SkillDto;
import java.util.List;

public interface CandidateProfileService {
    CandidateProfileResponse getCandidateProfileByEmail(String email);
    void updatePersonalProfile(String email, CandidateProfileRequest request);
    void addEducation(String email, EducationRequest request);
    void deleteEducation(String email, Integer educationId);
    void addExperience(String email, ExperienceRequest request);
    void deleteExperience(String email, Integer experienceId);
    void updateSkills(String email, List<Integer> skillIds);
    List<SkillDto> getAllSkills();
}
