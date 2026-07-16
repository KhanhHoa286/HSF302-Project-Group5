package vn.edu.fpt.hsf302_group5.service.impl.candidate;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileResponse;
import vn.edu.fpt.hsf302_group5.dto.candidate.CandidateProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.EducationRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.ExperienceRequest;
import vn.edu.fpt.hsf302_group5.dto.candidate.SkillDto;
import vn.edu.fpt.hsf302_group5.entity.CandidateProfile;
import vn.edu.fpt.hsf302_group5.entity.CandidateSkill;
import vn.edu.fpt.hsf302_group5.entity.CandidateSkillId;
import vn.edu.fpt.hsf302_group5.entity.Education;
import vn.edu.fpt.hsf302_group5.entity.Experience;
import vn.edu.fpt.hsf302_group5.entity.Skill;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.mapper.CandidateMapper;
import vn.edu.fpt.hsf302_group5.repository.candidate.CandidateProfileRepository;
import vn.edu.fpt.hsf302_group5.repository.candidate.EducationRepository;
import vn.edu.fpt.hsf302_group5.repository.candidate.ExperienceRepository;
import vn.edu.fpt.hsf302_group5.repository.skill.SkillRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.candidate.CandidateProfileService;
import vn.edu.fpt.hsf302_group5.service.user.CustomUserDetailsService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateProfileServiceImpl implements CandidateProfileService {

    private final UserRepository userRepository;
    private final CandidateProfileRepository candidateProfileRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final SkillRepository skillRepository;
    private final CandidateMapper candidateMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Override
    @Transactional(readOnly = true)
    public CandidateProfileResponse getCandidateProfileByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        CandidateProfile profile = candidateProfileRepository.findById(user.getUserId())
                .orElseGet(() -> {
                    CandidateProfile newProfile = CandidateProfile.builder()
                            .candidateId(user.getUserId())
                            .gender(user.getGender())
                            .build();
                    return candidateProfileRepository.save(newProfile);
                });

        return candidateMapper.toResponse(profile);
    }

    @Override
    @Transactional
    public void updatePersonalProfile(String email, CandidateProfileRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setGender(request.getGender());
        userRepository.save(user);

        CandidateProfile profile = candidateProfileRepository.findById(user.getUserId())
                .orElseGet(() -> CandidateProfile.builder().candidateId(user.getUserId()).build());

        profile.setDateOfBirth(request.getDateOfBirth());
        profile.setAddressDetail(request.getAddressDetail());
        profile.setSummary(request.getSummary());
        profile.setGender(request.getGender());
        candidateProfileRepository.save(profile);

        UserDetails newPrincipal = customUserDetailsService.loadUserByUsername(email);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            if (authentication instanceof OAuth2AuthenticationToken) {
                OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
                OAuth2AuthenticationToken newAuth = new OAuth2AuthenticationToken(
                        (OAuth2User) newPrincipal,
                        newPrincipal.getAuthorities(),
                        oauthToken.getAuthorizedClientRegistrationId() //xác định nhà cung cấp dịch vụ OAuth2 nào đã xác thực người dùng này.
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            } else {
                UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                        newPrincipal,
                        authentication.getCredentials(),
                        newPrincipal.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(newAuth);
            }
        }
    }

    @Override
    @Transactional
    public void addEducation(String email, EducationRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        CandidateProfile profile = candidateProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ cá nhân"));

        Education education = Education.builder()
                .candidateId(profile.getCandidateId())
                .schoolName(request.getSchoolName())
                .degree(request.getDegree())
                .major(request.getMajor())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        educationRepository.save(education);
    }

    @Override
    @Transactional
    public void deleteEducation(String email, Integer educationId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Education education = educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin học vấn"));

        if (!education.getCandidateId().equals(user.getUserId())) {
            throw new RuntimeException("Hành động không được phép");
        }

        educationRepository.delete(education);
    }

    @Override
    @Transactional
    public void addExperience(String email, ExperienceRequest request) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        CandidateProfile profile = candidateProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ cá nhân"));

        Experience experience = Experience.builder()
                .candidateId(profile.getCandidateId())
                .companyName(request.getCompanyName())
                .position(request.getPosition())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        experienceRepository.save(experience);
    }

    @Override
    @Transactional
    public void deleteExperience(String email, Integer experienceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Experience experience = experienceRepository.findById(experienceId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy kinh nghiệm làm việc"));

        if (!experience.getCandidateId().equals(user.getUserId())) {
            throw new RuntimeException("Hành động không được phép");
        }

        experienceRepository.delete(experience);
    }

    @Override
    @Transactional
    public void updateSkills(String email, List<Integer> skillIds) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        CandidateProfile profile = candidateProfileRepository.findById(user.getUserId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ cá nhân"));

        profile.getSkills().clear();
        candidateProfileRepository.saveAndFlush(profile);

        if (skillIds != null && !skillIds.isEmpty()) {
            for (Integer skillId : skillIds) {
                Skill skill = skillRepository.findById(skillId)
                        .orElseThrow(() -> new RuntimeException("Không tìm thấy kỹ năng"));

                CandidateSkill cs = CandidateSkill.builder()
                        .id(new CandidateSkillId(profile.getCandidateId(), skillId))
                        .candidateProfile(profile)
                        .skill(skill)
                        .build();

                profile.getSkills().add(cs);
            }
            candidateProfileRepository.save(profile);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<SkillDto> getAllSkills() {
        return skillRepository.findAll().stream()
                .map(candidateMapper::toSkillDtoFromEntity)
                .collect(Collectors.toList());
    }
}
