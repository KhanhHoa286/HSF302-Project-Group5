package vn.edu.fpt.hsf302_group5.mapper;

import org.mapstruct.*;
import org.springframework.stereotype.Component;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.JobPostFormRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.JobPostDetailResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring",nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface JobPostMapper {
    @Mapping(target="expiredDate", expression = "java(jobPostFormRequest.getExpiredDate() != null ? jobPostFormRequest.getExpiredDate().atTime(23,59,59) : null)")

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
            List<Integer> ids = new ArrayList<>();

            // Duyệt từng ông JobSkill, lấy ID của Skill bỏ vào list số nguyên
            for (JobSkill jobSkill : jobPost.getJobSkills()) {
                if (jobSkill.getSkill() != null) {
                    ids.add(jobSkill.getSkill().getSkillId());
                }
            }

            formRequest.setSkillsId(ids);
        }
    }

    @Mapping(target="company", source="recruiter.company")
    @Mapping(target="skill", ignore = true)
    JobPostDetailResponse toDtoJobPostDetail(JobPost jobPost);

    @AfterMapping
    private 
}
