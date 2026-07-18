package vn.edu.fpt.hsf302_group5.dto.industry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.edu.fpt.hsf302_group5.entity.enums.IndustryStatus;

@Getter
@Setter
@NoArgsConstructor
public class IndustryResponse {

    private Integer industryId;

    private String industryName;

    private IndustryStatus status;

    public IndustryResponse(Integer industryId, String industryName, IndustryStatus status) {
        this.industryId = industryId;
        this.industryName = industryName;
        this.status = status;
    }
}
