package vn.edu.fpt.hsf302_group5.service.impl.company;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.edu.fpt.hsf302_group5.dto.recruiter.request.CompanyProfileRequest;
import vn.edu.fpt.hsf302_group5.dto.recruiter.response.CompanyProfileResponse;
import vn.edu.fpt.hsf302_group5.entity.Company;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.mapper.CompanyMapper;
import vn.edu.fpt.hsf302_group5.repository.company.CompanyRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.cloudinary.CloudinaryService;
import vn.edu.fpt.hsf302_group5.service.company.CompanyService;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMapper companyMapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public CompanyProfileResponse getCompanyProfile(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (user.getRecruiter() == null) {
            throw new RuntimeException("Tài khoản không phải là Nhà tuyển dụng!");
        }

        Company company = user.getRecruiter().getCompany();
        if (company == null) {
            throw new RuntimeException("Nhà tuyển dụng chưa liên kết với công ty nào!");
        }

        return companyMapper.toResponse(company);
    }

    @Override
    @Transactional
    public void updateCompanyProfile(String userEmail, CompanyProfileRequest request) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (user.getRecruiter() == null) {
            throw new RuntimeException("Tài khoản không phải là Nhà tuyển dụng!");
        }

        Company company = user.getRecruiter().getCompany();
        if (company == null) {
            throw new RuntimeException("Nhà tuyển dụng chưa liên kết với công ty nào!");
        }

        // 1. Kiểm tra trùng lặp email nếu thay đổi email liên hệ
        if (!user.getEmail().equalsIgnoreCase(request.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email đã tồn tại trên hệ thống!");
            }
            user.setEmail(request.getEmail());
        }

        // 2. Cập nhật thông tin User liên kết
        user.setPhone(request.getPhone());
        user.setUpdatedAt(LocalDateTime.now());

        // 3. Cập nhật thông tin Company cơ bản bằng Mapper
        companyMapper.updateEntityFromRequest(request, company);
        company.setUpdatedAt(LocalDateTime.now());

        // 4. Xử lý tải ảnh logo lên Cloudinary nếu có file mới
        if (request.getLogoFile() != null && !request.getLogoFile().isEmpty()) {
            try {
                String newLogoUrl = cloudinaryService.uploadFile(request.getLogoFile());
                company.setLogoUrl(newLogoUrl);
            } catch (Exception e) {
                throw new RuntimeException("Lỗi tải logo lên hệ thống: " + e.getMessage());
            }
        }

        // 5. Lưu vào cơ sở dữ liệu
        companyRepository.save(company);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyProfileResponse getCompanyProfileById(Integer companyId) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy doanh nghiệp!"));
        return companyMapper.toResponse(company);
    }
}
