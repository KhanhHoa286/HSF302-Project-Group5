package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CompanyIndustry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyIndustry {
    @EmbeddedId
    private CompanyIndustryId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID", insertable = false, updatable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IndustryID", insertable = false, updatable = false)
    private Industry industry;
}
