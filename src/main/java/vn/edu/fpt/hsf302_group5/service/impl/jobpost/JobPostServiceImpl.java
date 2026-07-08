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
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;
import vn.edu.fpt.hsf302_group5.entity.Skill;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.mapper.JobPostMapper;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.repository.skill.SkillRepository;
import vn.edu.fpt.hsf302_group5.service.jobpost.JobPostService;
import vn.edu.fpt.hsf302_group5.specification.JobPostSpecification;
import vn.edu.fpt.hsf302_group5.util.AppConstants;

import java.math.BigDecimal;
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

    @Override
    public StatisticResponse getStatistic() {
        return jobPostRepository.getStatistic();
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
    public JobPost craeteJob(JobPostFormRequest jobPostForm) {
        //đợi xong login lấy id ở session
        JobPost jobPost = jobPostMapper.toEntity(jobPostForm);
        jobPost.setRecruiterId(2);
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
        return jobPostDetailResponse;
    }

    @Override
    public JobPost getJobPostById(Integer jobId) {
        return jobPostRepository.findById(jobId).orElse(null);
    }

    @Override
    public Page<JobPostDashboardResponse> getJobPostDashboard(String textSearch, JobStatus jobStatus, int page) {
        if (textSearch == null || textSearch.isEmpty()) {
            textSearch = null;
        }
        if (jobStatus == null) {
            jobStatus = null;
        }
        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_PAGE_PER_BLOCK);

        return jobPostRepository.getJobPostDashboard(textSearch, jobStatus, pageable);
    }

    @Override
    public Page<JobPostResponse> getJobPostsSpecification(int page, String filterLogicInOtherConditions, String filterLogicInSameConditions, List<String> searchKeyword, List<String> searchKeywordOperators, List<Integer> provinceId, List<String> provinceOperators, List<Integer> industryId, List<String> industryOperators, List<BigDecimal> salary, List<String> salaryOperators) {

        if (searchKeyword == null || searchKeyword.isEmpty()) {
            searchKeyword = new ArrayList<>();
        }
        if (provinceId == null || provinceId.isEmpty()) {
            provinceId = new ArrayList<>();
        }
        if (industryId == null || industryId.isEmpty()) {
            industryId = new ArrayList<>();
        }
        if (salary == null || salary.isEmpty()) {
            salary = new ArrayList<>();
        }

        Pageable pageable = PageRequest.of(page, AppConstants.NUMBER_JOB_PER_PAGE, Sort.by("postedDate").descending());

        Specification<JobPost> spec = Specification.unrestricted();

        Specification<JobPost> spectitle = Specification.unrestricted();
        for (int i = 0; i < searchKeyword.size(); i++) {
            if (i < searchKeywordOperators.size()) {
                Specification<JobPost> title = JobPostSpecification.buildTitleSpec(searchKeywordOperators.get(i), searchKeyword.get(i));
                if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                    spectitle = spectitle.and(title);
                } else {
                    spectitle = spectitle.or(title);
                }
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

        Specification<JobPost> specSalary = Specification.unrestricted();
        for (int i = 0; i < salary.size(); i++) {
            if (i < salaryOperators.size()) {
                Specification<JobPost> salarySpec = JobPostSpecification.buildSalarySpec(salaryOperators.get(i), salary.get(i));
                if (filterLogicInSameConditions.equalsIgnoreCase("AND")) {
                    specSalary = specSalary.and(salarySpec);
                } else {
                    specSalary = specSalary.or(salarySpec);
                }
            }
        }

        if (filterLogicInOtherConditions.equalsIgnoreCase("AND")) {
            spec = spec.and(spectitle).and(specProvince).and(specIndustry).and(specSalary);
        } else {
            spec = spec.or(spectitle).or(specProvince).or(specIndustry).or(specSalary);
        }

        Page<JobPost> jobPosts = jobPostRepository.findAll(spec, pageable);

        return jobPosts.map(jobPost -> {
            return jobPostMapper.toDto(jobPost)
                    ;
        });
    }

//    @Override
//    @Transactional
//    public void updateStatusJobPost(Integer jobPostId, JobStatus jobStatus) {
//        // tìm ra job có id đc truyền vào
//        JobPost jobPost = jobPostRepository.findById(jobPostId)
//                .orElseThrow(() -> new IllegalArgumentException("Job không tồn tại!"));
//
//        if(JobStatus.APPROVED == jobStatus) {
//            jobPost.setStatus(JobStatus.APPROVED);
//        }else if(JobStatus.CLOSED == jobStatus) {
//            jobPost.setStatus(JobStatus.CLOSED);
//        }
//        jobPostRepository.save(jobPost);
//    }


}
