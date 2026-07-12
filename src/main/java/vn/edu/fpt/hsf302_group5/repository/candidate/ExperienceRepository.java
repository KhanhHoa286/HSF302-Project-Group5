package vn.edu.fpt.hsf302_group5.repository.candidate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.edu.fpt.hsf302_group5.entity.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
}
