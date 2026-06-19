package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Industry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Industry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IndustryID")
    private Integer industryId;

    @Column(name = "IndustryName", nullable = false, unique = true, length = 100)
    private String industryName;

    // Relationships
    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CompanyIndustry> companyIndustries = new HashSet<>();

    @OneToMany(mappedBy = "industry", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<JobPost> jobPosts = new HashSet<>();
}
