package vn.edu.fpt.hsf302_group5.repository.jobskill;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.hsf302_group5.entity.JobSkill;

public interface JobSkillRepository extends JpaRepository<JobSkill, Integer> {
    void deleteByJobPostJobId(Integer jobId);
}