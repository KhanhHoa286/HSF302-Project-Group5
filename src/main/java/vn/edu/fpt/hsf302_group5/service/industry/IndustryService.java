package vn.edu.fpt.hsf302_group5.service.industry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryRequest;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

import java.util.List;

public interface IndustryService {

    List<IndustryResponse> getAllIndustryResponse();

    Page<IndustryResponse> getIndustryPage(String keyword, IndustryStatus status, Pageable pageable);

    void createIndustry(IndustryRequest request);

    void updateIndustry(Integer industryId, IndustryRequest request);

    void updateIndustryStatus(Integer industryId, IndustryStatus status);
}
