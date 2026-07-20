package vn.edu.fpt.hsf302_group5.repository.industry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.hsf302_group5.entity.Industry;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

import java.util.List;

public interface IndustryRepository extends JpaRepository<Industry, Integer> {

    List<Industry> findByStatusOrderByIndustryNameAsc(IndustryStatus status);

    @Query("""
            select i
            from Industry i
            where (:keyword is null or :keyword = '' or lower(i.industryName) like lower(concat('%', :keyword, '%')))
              and (:status is null or i.status = :status)
            """)
    Page<Industry> searchIndustries(@Param("keyword") String keyword,
                                    @Param("status") IndustryStatus status,
                                    Pageable pageable);

    boolean existsByIndustryNameIgnoreCase(String industryName);

    boolean existsByIndustryNameIgnoreCaseAndIndustryIdNot(String industryName, Integer industryId);
}
