package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "administrative_unit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativeUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "unit_id")
    private Integer unitId;

    @Column(name = "unit_code", nullable = false, unique = true, length = 20)
    private String unitCode;

    @Column(name = "unit_name", nullable = false, length = 100)
    private String unitName;

    @Column(name = "unit_level", nullable = false, length = 20)
    private String unitLevel;

    @Column(name = "province_id", nullable = false)
    private Integer provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    private Province province;

    @OneToMany(mappedBy = "administrativeUnit", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Company> companies = new HashSet<>();

    @OneToMany(mappedBy = "administrativeUnit", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<CandidateProfile> candidateProfiles = new HashSet<>();

    @OneToMany(mappedBy = "administrativeUnit", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<JobPost> jobPosts = new HashSet<>();
}
