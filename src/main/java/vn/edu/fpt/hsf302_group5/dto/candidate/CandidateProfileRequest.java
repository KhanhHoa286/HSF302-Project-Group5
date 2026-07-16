package vn.edu.fpt.hsf302_group5.dto.candidate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateProfileRequest {
    private String fullName;
    private String phone;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String addressDetail;
    private String summary;
}
