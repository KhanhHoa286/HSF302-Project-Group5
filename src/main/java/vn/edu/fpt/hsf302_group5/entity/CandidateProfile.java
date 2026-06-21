package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.Gender;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "candidate_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateProfile {
    @Id
    @Column(name = "candidate_id")
    private Integer candidateId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private User user;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 10)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address_detail", length = 255)
    private String addressDetail;

    @Column(name = "province_id")
    private Integer provinceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", insertable = false, updatable = false)
    private Province province;

    @Column(name = "administrative_unit_id")
    private Integer administrativeUnitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "administrative_unit_id", insertable = false, updatable = false)
    private AdministrativeUnit administrativeUnit;

    @Column(name = "summary", columnDefinition = "NVARCHAR(MAX)")
    private String summary;

    // Relationships
    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CandidateSkill> skills = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Education> educations = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Experience> experiences = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<CV> cvs = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<SavedJob> savedJobs = new HashSet<>();

    @OneToMany(mappedBy = "candidateProfile", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();
}

