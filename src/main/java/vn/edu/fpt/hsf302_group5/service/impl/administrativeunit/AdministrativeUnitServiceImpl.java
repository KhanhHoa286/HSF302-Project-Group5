package vn.edu.fpt.hsf302_group5.service.impl.administrativeunit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.administrativeunit.AdministrativeUnitResponse;
import vn.edu.fpt.hsf302_group5.repository.administrativeunit.AdministrativeUnitRepository;
import vn.edu.fpt.hsf302_group5.service.administrativeunit.AdministrativeUnitService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdministrativeUnitServiceImpl implements AdministrativeUnitService {

    private final AdministrativeUnitRepository administrativeUnitRepository;

    @Override
    public List<AdministrativeUnitResponse> getByProvinceId(Integer provinceId) {
        return administrativeUnitRepository.findByProvinceId(provinceId);
    }
}
