package vn.edu.fpt.hsf302_group5.repository.company;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.dto.home.HomeCompanyResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus;

import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findTop5ByOrderByCreatedAtDesc();

    @Query("""
    select new vn.edu.fpt.hsf302_group5.dto.home.HomeCompanyResponse(
        c.companyId, c.companyName, c.logoUrl, COUNT(a.applicationId)
    )
    from Company c 
    left join c.recruiter r 
    left join r.jobPosts j 
    left join j.applications a 
    where c.status = vn.edu.fpt.hsf302_group5.entity.enums.CompanyStatus.ACTIVE 
    group by c.companyId, c.companyName, c.logoUrl 
    order by COUNT(a.applicationId) desc
    """)
    Page<HomeCompanyResponse> findTopCompanies(Pageable pageable);

    @Query("""
    select c from Company c 
     left join c.recruiter r 
      left join r.user u 
          where (:status is null or c.status = :status)
              and (:keyword is null or 
                   lower(c.companyName) like lower(concat('%',:keyword, '%')) or 
                   lower(u.email) like lower(concat('%',:keyword, '%')))
    """)
    Page<Company> findAllCompany(
            @Param("status") CompanyStatus status,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    long countByStatus(CompanyStatus status);
}
