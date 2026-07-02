package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "applications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Integer applicationId;

    @Column(name = "candidate_id", nullable = false)
    private Integer candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "job_id", nullable = false)
    private Integer jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobPost jobPost;

    @Column(name = "cv_id", nullable = false)
    private Integer cvId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", insertable = false, updatable = false)
    private CV cv;

    @Column(name = "applied_date", nullable = false)
    @Builder.Default
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "cover_letter", columnDefinition = "NVARCHAR(MAX)")
    private String coverLetter;

    @Column(name = "status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "note", length = 500)
    private String note;

    // Relationships
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ApplicationStatusHistory> statusHistories = new HashSet<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Interview> interviews = new HashSet<>();

    public String getCandidateFullName() {
        return (candidateProfile != null && candidateProfile.getUser() != null) 
            ? candidateProfile.getUser().getFullName() : null;
    }

    public String getCandidateEmail() {
        return (candidateProfile != null && candidateProfile.getUser() != null) 
            ? candidateProfile.getUser().getEmail() : null;
    }

    public String getCandidatePhone() {
        return (candidateProfile != null && candidateProfile.getUser() != null) 
            ? candidateProfile.getUser().getPhone() : null;
    }

    public String getCandidateAvatarUrl() {
        return (candidateProfile != null && candidateProfile.getUser() != null) 
            ? candidateProfile.getUser().getAvatarUrl() : null;
    }

    public String getJobTitle() {
        return (jobPost != null) ? jobPost.getTitle() : null;
    }

    public java.time.LocalDate getCandidateDateOfBirth() {
        return (candidateProfile != null) ? candidateProfile.getDateOfBirth() : null;
    }

    public String getCandidateAddressDetail() {
        return (candidateProfile != null) ? candidateProfile.getAddressDetail() : null;
    }

    public String getCandidateProvinceName() {
        return (candidateProfile != null && candidateProfile.getProvince() != null) 
            ? candidateProfile.getProvince().getProvinceName() : null;
    }

    public String getCandidateSummary() {
        return (candidateProfile != null) ? candidateProfile.getSummary() : null;
    }

    public java.util.List<String> getCandidateSkills() {
        if (candidateProfile == null || candidateProfile.getSkills() == null) {
            return java.util.List.of();
        }
        return candidateProfile.getSkills().stream()
                .map(cs -> cs.getSkill().getSkillName())
                .toList();
    }

    public java.util.Set<Education> getCandidateEducations() {
        return (candidateProfile != null) ? candidateProfile.getEducations() : null;
    }

    public java.util.Set<Experience> getCandidateExperiences() {
        return (candidateProfile != null) ? candidateProfile.getExperiences() : null;
    }

    public String getCvName() {
        return (cv != null) ? cv.getCvName() : null;
    }

    public String getCvUrl() {
        return (cv != null) ? cv.getFileUrl() : null;
    }

    public String getCandidateGender() {
        if (candidateProfile != null) {
            if (candidateProfile.getUser() != null && candidateProfile.getUser().getGender() != null) {
                return candidateProfile.getUser().getGender().name();
            }
            if (candidateProfile.getGender() != null) {
                return candidateProfile.getGender().name();
            }
        }
        return null;
    }
}

