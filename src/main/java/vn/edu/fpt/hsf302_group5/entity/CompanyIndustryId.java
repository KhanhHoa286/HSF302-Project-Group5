package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CompanyIndustryId implements Serializable {
    @Column(name = "CompanyID")
    private Integer companyId;

    @Column(name = "IndustryID")
    private Integer industryId;
}
