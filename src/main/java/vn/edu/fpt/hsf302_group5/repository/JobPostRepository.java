package vn.edu.fpt.hsf302_group5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.dto.StatisticResponse;
import vn.edu.fpt.hsf302_group5.entity.JobPost;

@Repository
public interface JobPostRepository extends JpaRepository<JobPost,Integer> {
    @Query("""
            SELECT new vn.edu.fpt.hsf302_group5.dto.StatisticResponse(
            COUNT(j),
            COUNT(CASE WHEN j.status = vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.APPROVED THEN  1 END),
            COUNT(CASE WHEN j.status = vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.PENDING THEN 1 END),
            COUNT(CASE WHEN j.status IN (vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.CLOSED, 
            vn.edu.fpt.hsf302_group5.entity.enums.JobStatus.REJECTED) THEN 1 END)
            ) 
            FROM JobPost j 
""")
    StatisticResponse getStatistic();
}
