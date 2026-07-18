package vn.edu.fpt.hsf302_group5.service.impl.industry;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryRequest;
import vn.edu.fpt.hsf302_group5.dto.industry.IndustryResponse;
import vn.edu.fpt.hsf302_group5.entity.Industry;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;
import vn.edu.fpt.hsf302_group5.repository.industry.IndustryRepository;
import vn.edu.fpt.hsf302_group5.service.industry.IndustryService;

import java.util.List;

@RequiredArgsConstructor
@Service
public class IndustryServiceImpl implements IndustryService {

    private final IndustryRepository industryRepository;

    @Override
    public List<IndustryResponse> getAllIndustryResponse() {
        return industryRepository.findAllActiveIndustryResponse();
    }

    @Override
    public Page<IndustryResponse> getIndustryPage(String keyword, IndustryStatus status, Pageable pageable) {
        String normalizedKeyword = normalizeKeyword(keyword);
        return industryRepository.searchIndustryResponses(normalizedKeyword, status, pageable);
    }

    @Override
    public void createIndustry(IndustryRequest request) {
        String industryName = normalizeName(request.getIndustryName());
        if (industryRepository.existsByIndustryNameIgnoreCase(industryName)) {
            throw new IllegalArgumentException("Tên ngành nghề đã tồn tại.");
        }

        Industry industry = Industry.builder()
                .industryName(industryName)
                .status(request.getStatus())
                .build();
        industryRepository.save(industry);
    }

    @Override
    public void updateIndustry(Integer industryId, IndustryRequest request) {
        Industry industry = getIndustry(industryId);
        String industryName = normalizeName(request.getIndustryName());
        if (industryRepository.existsByIndustryNameIgnoreCaseAndIndustryIdNot(industryName, industryId)) {
            throw new IllegalArgumentException("Tên ngành nghề đã tồn tại.");
        }

        industry.setIndustryName(industryName);
        industry.setStatus(request.getStatus());
        industryRepository.save(industry);
    }

    @Override
    public void updateIndustryStatus(Integer industryId, IndustryStatus status) {
        Industry industry = getIndustry(industryId);
        industry.setStatus(status);
        industryRepository.save(industry);
    }

    private Industry getIndustry(Integer industryId) {
        return industryRepository.findById(industryId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy ngành nghề với ID: " + industryId));
    }

    private String normalizeName(String industryName) {
        return industryName == null ? "" : industryName.trim();
    }

    private String normalizeKeyword(String keyword) {
        String normalized = keyword == null ? null : keyword.trim();
        return normalized == null || normalized.isEmpty() ? null : normalized;
    }
}
