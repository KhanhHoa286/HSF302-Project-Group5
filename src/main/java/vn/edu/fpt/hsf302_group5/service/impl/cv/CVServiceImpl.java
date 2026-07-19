package vn.edu.fpt.hsf302_group5.service.impl.cv;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.hsf302_group5.entity.CV;
import vn.edu.fpt.hsf302_group5.entity.CandidateProfile;
import vn.edu.fpt.hsf302_group5.entity.User;
import vn.edu.fpt.hsf302_group5.repository.cv.CVRepository;
import vn.edu.fpt.hsf302_group5.repository.user.UserRepository;
import vn.edu.fpt.hsf302_group5.service.cloudinary.CloudinaryService;
import vn.edu.fpt.hsf302_group5.service.cv.CVService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CVServiceImpl implements CVService {

    private final CVRepository cvRepository;
    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public List<CV> getCVsByCandidateId(Integer candidateId) {
        return cvRepository.findByCandidateId(candidateId);
    }

    @Override
    @Transactional
    public CV uploadCV(Integer candidateId, MultipartFile file, String customName) throws IOException {
        User user = userRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng!"));

        if (user.getCandidateProfile() == null) {
            CandidateProfile profile = CandidateProfile.builder()
                    .candidateId(candidateId)
                    .user(user)
                    .build();
            user.setCandidateProfile(profile);
            userRepository.save(user);
        }

        String fileUrl = cloudinaryService.uploadFile(file);
        
        CV cv = CV.builder()
                .candidateId(candidateId)
                .cvName(customName != null && !customName.trim().isEmpty() ? customName.trim() : file.getOriginalFilename())
                .fileName(file.getOriginalFilename())
                .fileUrl(fileUrl)
                .uploadedAt(LocalDateTime.now())
                .build();

        return cvRepository.save(cv);
    }

    @Override
    @Transactional
    public void deleteCV(Integer candidateId, Integer cvId) {
        CV cv = cvRepository.findById(cvId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy CV với ID: " + cvId));

        if (!cv.getCandidateId().equals(candidateId)) {
            throw new SecurityException("Bạn không có quyền xóa CV này!");
        }

        cvRepository.delete(cv);
    }

    @Override
    @Transactional(readOnly = true)
    public CV getCVById(Integer cvId) {
        return cvRepository.findById(cvId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy CV với ID: " + cvId));
    }
}
