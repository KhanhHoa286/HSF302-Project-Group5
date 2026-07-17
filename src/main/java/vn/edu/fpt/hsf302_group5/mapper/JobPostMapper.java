package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.*;
import vn.edu.fpt.hsf302_group5.dto.job_post.JobPostResponse;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDetailRecruiterResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        imports = {vn.edu.fpt.hsf302_group5.util.JobPostUtil.class}
)
public interface JobPostMapper {
    @Mapping(target="expiredDate", expression = "java(jobPostFormRequest.getExpiredDate() != null ? jobPostFormRequest.getExpiredDate().atTime(23,59,59) : null)")

    JobPost toEntity(JobPostFormRequest jobPostFormRequest);

    @Mapping(source = "title", target = "jobTitle")
    @Mapping(source = "recruiter.company.companyName", target = "companyName")
    @Mapping(source = "province.provinceName", target = "companyProvinceAddress")
    @Mapping(source = "expiredDate", target = "expirationDate")
    @Mapping(source = "recruiter.company.logoUrl", target = "companyLogoUrl")
    JobPostResponse toDto(JobPost jobPost);

    JobPost toEntityCreateForm(JobPostFormRequest jobPostFormRequest);

    @Mapping(target="expiredDate", expression = "java(jobPostFormRequest.getExpiredDate() != null ? jobPostFormRequest.getExpiredDate().atTime(23,59,59) : jobPost.getExpiredDate())")

    void updateJob(JobPostFormRequest jobPostFormRequest, @MappingTarget JobPost jobPost);

    @Mapping(target = "skillsId", ignore = true)
    @Mapping(target = "administrativeUnitId", source = "administrativeUnitId")
    @Mapping(target = "jobPostId", source = "jobId")
    @Mapping(target = "expiredDate", expression = "java(jobPost.getExpiredDate() != null ? jobPost.getExpiredDate().toLocalDate() : null)")

    JobPostFormRequest toDtoUpdateForm(JobPost jobPost);

    @AfterMapping
    default void mapJobSkillsToIds(JobPost jobPost, @MappingTarget JobPostFormRequest formRequest) {
        if (jobPost.getJobSkills() != null) {
            List<Integer> listIds = new ArrayList<>();

            // Duyệt từng ông JobSkill, lấy ID của Skill bỏ vào list số nguyên
            for (JobSkill jobSkill : jobPost.getJobSkills()) {
                if (jobSkill.getSkill() != null) {
                    listIds.add(jobSkill.getSkill().getSkillId());
                }
            }

            formRequest.setSkillsId(listIds);
        }
    }

    @Mapping(target="company", source="recruiter.company")
    @Mapping(target="skillsName", ignore = true)
    @Mapping(target = "salaryDisplay", expression = "java(JobPostUtil.getSalaryDisplay(jobPost.getSalaryMin(),jobPost.getSalaryMax()))")
    JobPostDetailRecruiterResponse toDtoJobPostDetail(JobPost jobPost);

    @AfterMapping
    default void mapJobSkillsToString(JobPost jobPost, @MappingTarget JobPostDetailRecruiterResponse jobPostDetailResponse) {
        if(jobPost.getJobSkills() != null) {
          List<String> listSkillName = new ArrayList<>();

          for(JobSkill jobSkill : jobPost.getJobSkills()) {
              listSkillName.add(jobSkill.getSkill().getSkillName());
          }
          jobPostDetailResponse.setSkillsName(listSkillName);
        }
    }
}
