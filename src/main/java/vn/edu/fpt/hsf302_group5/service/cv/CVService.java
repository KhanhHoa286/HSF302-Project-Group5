package vn.edu.fpt.hsf302_group5.service.cv;

import org.springframework.web.multipart.MultipartFile;
import vn.edu.fpt.hsf302_group5.entity.CV;
import java.io.IOException;
import java.util.List;

public interface CVService {
    List<CV> getCVsByCandidateId(Integer candidateId);
    CV uploadCV(Integer candidateId, MultipartFile file, String customName) throws IOException;
    void deleteCV(Integer candidateId, Integer cvId);
    CV getCVById(Integer cvId);
}
