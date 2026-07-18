package vn.edu.fpt.hsf302_group5.repository.industry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.entity.Industry;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {

    @Query("""
            select new vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse(i.industryId, i.industryName, i.status)
            from Industry i
            where i.status = vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus.ACTIVE
            order by i.industryName asc
            """)
    List<IndustryResponse> findAllActiveIndustryResponse();

    @Query("""
            select new vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse(i.industryId, i.industryName, i.status)
            from Industry i
            where (:keyword is null or lower(i.industryName) like lower(concat('%', :keyword, '%')))
              and (:status is null or i.status = :status)
            """)
    Page<IndustryResponse> searchIndustryResponses(@Param("keyword") String keyword,
                                                   @Param("status") IndustryStatus status,
                                                   Pageable pageable);

    boolean existsByIndustryNameIgnoreCase(String industryName);

    boolean existsByIndustryNameIgnoreCaseAndIndustryIdNot(String industryName, Integer industryId);
}
