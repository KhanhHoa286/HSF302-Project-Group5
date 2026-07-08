package vn.edu.fpt.hsf302_group5.repository.jobpost;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.JobPost;

@Repository
public interface JobPostSpecificationRepository extends JpaSpecificationExecutor<JobPost> {

}
