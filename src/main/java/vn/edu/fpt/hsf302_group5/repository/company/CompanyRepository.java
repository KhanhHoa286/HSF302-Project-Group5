package vn.edu.fpt.hsf302_group5.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.fpt.hsf302_group5.entity.Company;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    List<Company> findTop5ByOrderByCreatedAtDesc();
}
