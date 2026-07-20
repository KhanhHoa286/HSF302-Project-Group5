package vn.edu.fpt.hsf302_group5.service.impl.jobpost;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDetailRecruiterResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;
import vn.edu.fpt.hsf302_group5.entity.Skill;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.mapper.JobPostMapper;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.repository.jobskill.JobSkillRepository;
import vn.edu.fpt.hsf302_group5.repository.skill.SkillRepository;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.specification.JobPostSpecification;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JobPostServiceImpl implements JobPostService {
    private final JobPostRepository jobPostRepository;
    private final JobPostMapper jobPostMapper;
    private final SkillRepository skillRepository;
    private final JobSkillRepository jobSkillRepository;

    @Override
    public StatisticResponse getStatistic(Integer recruiterId) {
        return jobPostRepository.getStatistic(recruiterId);
    }

    @Override
    public Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page) {
        if (searchKeyword == null || searchKeyword.isBlank()) {
            searchKeyword = null;
        }
        if (industryId == null || industryId == -1) {
            industryId = null;
        }
        if (provinceId == null || provinceId == -1) {
            provinceId = null;
        }
        if (minSalary == null) {
            minSalary = null;
        }
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_JOB_PER_PAGE);
        return jobPostRepository.getJobPostResponseByFilter(searchKeyword, industryId, provinceId, minSalary, pageable);
    }

    @Override
    @Transactional
    public JobPost craeteJob(JobPostFormRequest jobPostForm, int userId) {
        //đợi xong login lấy id ở session
        JobPost jobPost = jobPostMapper.toEntityCreateForm(jobPostForm);
        jobPost.setRecruiterId(userId);
        jobPost.setStatus(JobStatus.PENDING);
        //insert dữ liệu vào job_skill
        if (jobPostForm.getSkillsId() != null && !jobPostForm.getSkillsId().isEmpty()) {
            Set<JobSkill> setJobSkills = new HashSet<>();
            for (Integer skillId : jobPostForm.getSkillsId()) {
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobPost(jobPost);

                Skill skill = skillRepository.getReferenceById(skillId);
                jobSkill.setSkill(skill);

                setJobSkills.add(jobSkill);
            }
            jobPost.setJobSkills(setJobSkills);
        }
        //
        return jobPostRepository.save(jobPost);
    }

    @Override
    public JobPostDetailResponse getJobPostDetaiDTOByJobPostId(Integer jobPostId) {
        JobPostDetailResponse jobPostDetailResponse = jobPostRepository.getJobPostDetaiDTOByJobPostId(jobPostId);
        if (jobPostDetailResponse != null) {
            jobPostDetailResponse.setRequiredSkills(jobPostRepository.findSkillNamesByJobPostId(jobPostId));
        }
        return jobPostDetailResponse;
    }

    @Override
    public JobPost getJobPostById(Integer jobId) {
        return jobPostRepository.findById(jobId).orElse(null);
    }

    @Override
    public Page<JobPostDashboardResponse> getJobPostDashboard(String textSearch, JobStatus jobStatus, int page, int recruiterId) {
        if (textSearch == null || textSearch.isEmpty()) {
            textSearch = null;
        }
        if (jobStatus == null) {
            jobStatus = null;
        }
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_PAGE_PER_BLOCK);

        return jobPostRepository.getJobPostDashboard(recruiterId, textSearch, jobStatus, pageable);
    }

    @Override
    public Page<JobPostResponse> getJobPostsSpecification(int page, String filterLogicInOtherConditions, String filterLogicInSameConditions, List<String> searchKeyword, List<String> searchKeywordOperators, List<Integer> provinceId, List<String> provinceOperators, List<Integer> industryId, List<String> industryOperators, List<Integer> companyId, List<String> companyOperators, List<BigDecimal> salary, List<String> salaryOperators, List<LocalDate> expireDate, List<String> operators, List<String> sort, List<String> sortOperator, CustomUserDetailsResponse userDetails) {

        if (searchKeyword == null) {
            searchKeyword = new ArrayList<>();
        } else {
            searchKeyword.removeIf(k -> k == null || k.trim().isEmpty());
        }
        if (provinceId == null) {
            provinceId = new ArrayList<>();
        } else {
            provinceId.removeIf(java.util.Objects::isNull);
        }
        if (industryId == null) {
            industryId = new ArrayList<>();
        } else {
            industryId.removeIf(java.util.Objects::isNull);
        }
        if (companyId == null) {
            companyId = new ArrayList<>();
        } else {
            companyId.removeIf(java.util.Objects::isNull);
        }
        if (salary == null) {
            salary = new ArrayList<>();
        } else {
            salary.removeIf(java.util.Objects::isNull);
        }
        if (expireDate == null) {
            expireDate = new ArrayList<>();
        } else {
            expireDate.removeIf(java.util.Objects::isNull);
        }

        List<Sort.Order> orders = new ArrayList<>();
        if (sort != null) {
            for (int i = 0; i < sort.size(); i++) {
                String field = sort.get(i);
                if ("applications".equals(field)) {
                    field = "applicationCount";
                }
                String direction = (sortOperator != null && i < sortOperator.size()) ? sortOperator.get(i) : "desc";
                Sort.Direction dir = "asc".equalsIgnoreCase(direction) ? Sort.Direction.ASC : Sort.Direction.DESC;
                orders.add(new Sort.Order(dir, field));
            }
        }
        Pageable pageable = orders.isEmpty()
                ? PageRequest.of(page, AppConstants.NUMBER_JOB_PER_PAGE)
                : PageRequest.of(page, AppConstants.NUMBER_JOB_PER_PAGE, Sort.by(orders));

        Specification<JobPost> spec = Specification.unrestricted(); 

        Specification<JobPost> spectitle = Specification.unrestricted();
        for (int i = 0; i < searchKeyword.size(); i++) {
            String op = (searchKeywordOperators != null && i < searchKeywordOperators.size()) ? searchKeywordOperators.get(i) : "contains";
            Specification<JobPost> title = JobPostSpecification.buildTitleSpec(op, searchKeyword.get(i));
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                spectitle = spectitle.and(title);
            } else {
                spectitle = spectitle.or(title);
            }
        }

        Specification<JobPost> specExpire = Specification.unrestricted();
        for (int i = 0; i < expireDate.size(); i++) {
            String op = (operators != null && i < operators.size()) ? operators.get(i) : "=";
            Specification<JobPost> expireSpec = JobPostSpecification.buildExpireSpec(expireDate.get(i), op);
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                specExpire = specExpire.and(expireSpec);
            } else {
                specExpire = specExpire.or(expireSpec);
            }
        }

        Specification<JobPost> specProvince = Specification.unrestricted();
        for (int i = 0; i < provinceId.size(); i++) {
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                specProvince = specProvince.and(JobPostSpecification.hasProvice(provinceId.get(i)));
            } else {
                specProvince = specProvince.or(JobPostSpecification.hasProvice(provinceId.get(i)));
            }
        }

        Specification<JobPost> specIndustry = Specification.unrestricted();
        for (int i = 0; i < industryId.size(); i++) {
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                specIndustry = specIndustry.and(JobPostSpecification.hasIndustry(industryId.get(i)));
            } else {
                specIndustry = specIndustry.or(JobPostSpecification.hasIndustry(industryId.get(i)));
            }
        }

        Specification<JobPost> specCompany = Specification.unrestricted();
        for (int i = 0; i < companyId.size(); i++) {
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                specCompany = specCompany.and(JobPostSpecification.hasCompany(companyId.get(i)));
            } else {
                specCompany = specCompany.or(JobPostSpecification.hasCompany(companyId.get(i)));
            }
        }

        Specification<JobPost> specSalary = Specification.unrestricted();
        for (int i = 0; i < salary.size(); i++) {
            String op = (salaryOperators != null && i < salaryOperators.size()) ? salaryOperators.get(i) : ">=";
            Specification<JobPost> salarySpec = JobPostSpecification.buildSalarySpec(op, salary.get(i));
            if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                specSalary = specSalary.and(salarySpec);
            } else {
                specSalary = specSalary.or(salarySpec);
            }
        }

        if (filterLogicInOtherConditions.equalsIgnoreCase("AND")) {
            spec = spec.and(spectitle).and(specProvince).and(specIndustry).and(specCompany).and(specSalary).and(specExpire);
        } else {
            spec = spec.or(spectitle).or(specProvince).or(specIndustry).or(specCompany).or(specSalary).or(specExpire);
        }

        // Lọc mặc định: Chỉ hiển thị công việc đang tuyển (APPROVED) và chưa hết hạn nộp
        spec = spec.and(JobPostSpecification.isApproved())
                   .and(JobPostSpecification.isNotExpired());

        if (userDetails != null) {
            spec = spec.and(JobPostSpecification.filterJobNotApply(userDetails));
        }

        Page<JobPost> jobPosts = jobPostRepository.findAll(spec, pageable);

        return jobPosts.map(jobPost -> {
            return jobPostMapper.toDto(jobPost);
        });
    }

    @Override
    @Transactional
    public JobPost updateJob(JobPostFormRequest jobPostForm) {
        // tìm ra job id đó
        JobPost jobPost = jobPostRepository.findById(jobPostForm.getJobPostId())
                .orElseThrow(() -> new IllegalArgumentException("Job không tồn tại!"));
        // bắn nó sang cho mapper để tiến hành ghi đè thuộc tính mới lên cho jobPost này
        jobPostMapper.updateJob(jobPostForm, jobPost);
        // sau khi ghi đè xong thì ta có 1 jobPost với các thuộc tính mới và giữ nguyên jobPost id,...
        // tiếp theo là lấy mảng skills mới trong jobPost này ra tinh chỉnh lại
        //insert dữ liệu vào job_skill
        jobSkillRepository.deleteByJobPostJobId(jobPost.getJobId());
        if (jobPostForm.getSkillsId() != null && !jobPostForm.getSkillsId().isEmpty()) {
            List<JobSkill> newSkillsToSave = new ArrayList<>();

            for (Integer skillId : jobPostForm.getSkillsId()) {
                JobSkill jobSkill = new JobSkill();
                jobSkill.setJobPost(jobPost);
                jobSkill.setSkill(skillRepository.getReferenceById(skillId));

                newSkillsToSave.add(jobSkill);
            }
            jobSkillRepository.saveAll(newSkillsToSave);
        }
        //
        return jobPostRepository.save(jobPost);
    }

    @Override
    public JobPostFormRequest updateFormRequest(Integer id) {
        JobPost jobPost = jobPostRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Job không tồn tại!"));
        JobPostFormRequest jobPostFormRequest = jobPostMapper.toDtoUpdateForm(jobPost);
        return jobPostFormRequest;
    }

    @Override
    public JobPostDetailRecruiterResponse getJobPostDetail(Integer id) {
        //
        JobPost jobPost = jobPostRepository.findByJobId(id).orElseThrow(() -> new IllegalArgumentException("Job không tồn tại!"));
        //
        return jobPostMapper.toDtoJobPostDetail(jobPost);
    }

    @Override
    @Transactional
    public void updateStatusJob(Integer jobId, String status) {
        if(JobStatus.APPROVED.toString().equals(status)) {
            jobPostRepository.updateStatusJob(jobId,JobStatus.CLOSED);
        }else if (JobStatus.CLOSED.toString().equals(status)){
            jobPostRepository.updateStatusJob(jobId,JobStatus.APPROVED);
        }
    }
}
