package vn.edu.fpt.hsf302_group5.service.impl.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.CompanyDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.admin.AdminJobDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import vn.edu.fpt.hsf302_group5.entity.enums.UserRole;
import vn.edu.fpt.hsf302_group5.mapper.AdminMapper;
import vn.edu.fpt.hsf302_group5.repository.company.CompanyRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.repository.jobpost.JobPostRepository;
import vn.edu.fpt.hsf302_group5.service.user.AdminService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final JobPostRepository jobPostRepository;
    private final AdminMapper adminMapper;

    public AdminServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
            JobPostRepository jobPostRepository, AdminMapper adminMapper) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.jobPostRepository = jobPostRepository;
        this.adminMapper = adminMapper;
    }

    @Override
    public long countCandidates() {
        return userRepository.countByRole_RoleName(UserRole.CANDIDATE.name());
    }

    @Override
    public long countRecruiters() {
        return userRepository.countByRole_RoleName(UserRole.RECRUITER.name());
    }

    @Override
    public long countCompanies() {
        return companyRepository.count();
    }

    @Override
    public long countJobPosts() {
        return jobPostRepository.count();
    }

    @Override
    public List<JobPostDashboardResponse> getRecentPendingJobs() {
        List<JobPost> jobPosts = jobPostRepository.findTop5ByStatusOrderByPostedDateDesc(JobStatus.PENDING);
        List<JobPostDashboardResponse> recentPendingJobs = new ArrayList<>();
        for (JobPost job : jobPosts) {
            recentPendingJobs.add(adminMapper.toJobPostDashboardResponse(job));
        }
        return recentPendingJobs;
    }

    @Override
    public List<CompanyDashboardResponse> getRecentCompanies() {
        List<Company> companies = companyRepository.findTop5ByOrderByCreatedAtDesc();
        List<CompanyDashboardResponse> recentCompanies = new ArrayList<>();
        for (Company comp : companies) {
            recentCompanies.add(adminMapper.toCompanyDashboardResponse(comp));
        }
        return recentCompanies;
    }

    @Override
    public Page<JobPostDashboardResponse> getJobPostForApproval(String keyword, JobStatus status, Pageable pageable) {
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }
        Page<JobPost> entityPage = jobPostRepository.findAllForApproval(status, keyword, pageable);
        return entityPage.map(adminMapper::toJobPostDashboardResponse);
    }

    @Override
    public long countJobPostsByStatus(JobStatus jobStatus) {
        return jobPostRepository.countByStatus(jobStatus);
    }

    @Override
    public AdminJobDetailResponse getJobPostById(Integer id) {
        JobPost jobPost = jobPostRepository.findByJobId(id)
                .orElseThrow(() -> new IllegalArgumentException("khong tim thay tin tuyen dung voi ID: " + id));
        return adminMapper.toAdminJobDetailResponse(jobPost);
    }

    @Override
    public void updateJobPostStatus(Integer id, JobStatus status, String comment) {
        JobPost jobPost = jobPostRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(" khong tim thay tin tuyen dung voi ID: " + id));
        jobPost.setStatus(status);
        jobPost.setAdminComment(comment);
        jobPostRepository.save(jobPost);
    }

    @Override
    public Page<CompanyDashboardResponse> getAllCompanies(String keyword, CompanyStatus status, Pageable pageable) {
        if (keyword != null) {
            keyword = keyword.trim();
            if (keyword.isEmpty()) {
                keyword = null;
            }
        }
        Page<Company> entityPage = companyRepository.findAllCompany(status, keyword, pageable);
        return entityPage.map(adminMapper::toCompanyDashboardResponse);
    }

    @Override
    public long countCompaniesByStatus(CompanyStatus status) {
        return companyRepository.countByStatus(status);
    }

    @Override
    public CompanyDetailResponse getCompanyById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay doanh nghiep voi ID:" + companyId));
        return adminMapper.toCompanyDetailResponse(company);
    }

    @Override
    public void updateCompanyStatus(Integer companyId, CompanyStatus status) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Khong tim thay doanh nghiep voi ID:" + companyId));
        company.setStatus(status);
        company.setUpdatedAt(LocalDateTime.now());
        companyRepository.save(company);
    }

}
