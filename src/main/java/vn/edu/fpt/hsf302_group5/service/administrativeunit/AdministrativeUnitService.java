package vn.edu.fpt.hsf302_group5.service.administrativeunit;

import vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse;
import java.util.List;

public interface AdministrativeUnitService {
    List<AdministrativeUnitResponse> getByProvinceId(Integer provinceId);
}
