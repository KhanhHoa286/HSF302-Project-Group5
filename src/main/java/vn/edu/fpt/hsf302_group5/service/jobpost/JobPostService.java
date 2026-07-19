package vn.edu.fpt.hsf302_group5.service.jobpost;

import org.springframework.data.domain.Page;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDashboardResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDetailRecruiterResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.StatisticResponse;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.dto.user.CustomUserDetailsResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface JobPostService {
      StatisticResponse getStatistic();

      Page<JobPostResponse> getJobPostsByFilter(String searchKeyword, Integer industryId, Integer provinceId, BigDecimal minSalary, int page);

      JobPost craeteJob(JobPostFormRequest jobPostForm, int userId);

      JobPostDetailResponse getJobPostDetaiDTOByJobPostId(Integer jobPostId);

      JobPost getJobPostById(Integer jobId);

      Page<JobPostDashboardResponse> getJobPostDashboard(String textSearch, JobStatus jobStatus, int page,int recruiterId);

      Page<JobPostResponse> getJobPostsSpecification(int page, String filterLogicInOtherConditions, String filterLogicInSameConditions, List<String> searchKeyword, List<String> searchKeywordOperators, List<Integer> provinceId, List<String> provinceOperators, List<Integer> industryId, List<String> industryOperators, List<Integer> companyId, List<String> companyOperators, List<BigDecimal> salary, List<String> operators, List<LocalDate> expireDate, List<String> salaryOperators, List<String> sort, List<String> sortOperator, CustomUserDetailsResponse userDetails);

//      void updateStatusJobPost(Integer jobPostId,JobStatus jobStatus);

      JobPost updateJob(JobPostFormRequest jobPostForm);

      JobPostFormRequest updateFormRequest(Integer id);

      JobPostDetailRecruiterResponse getJobPostDetail(Integer id);

      void updateStatusJob(Integer jobId, String status);
}
