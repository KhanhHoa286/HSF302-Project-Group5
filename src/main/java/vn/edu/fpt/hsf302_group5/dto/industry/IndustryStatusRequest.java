package vn.edu.fpt.hsf302_group5.dto.industry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

@Getter
@Setter
@NoArgsConstructor
public class IndustryStatusRequest {
    private IndustryStatus status;
}
