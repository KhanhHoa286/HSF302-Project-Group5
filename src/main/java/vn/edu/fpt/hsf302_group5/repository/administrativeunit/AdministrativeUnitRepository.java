package vn.edu.fpt.hsf302_group5.repository.administrativeunit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse;
import vn.edu.fpt.hsf302_group5.entity.AdministrativeUnit;

import java.util.List;

public interface AdministrativeUnitRepository extends JpaRepository<AdministrativeUnit, Integer> {

    @Query("""
        select new vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse(a.unitId, a.unitName)
        from AdministrativeUnit a
        where a.provinceId = :provinceId
    """)
    List<AdministrativeUnitResponse> findByProvinceId(@Param("provinceId") Integer provinceId);
}
