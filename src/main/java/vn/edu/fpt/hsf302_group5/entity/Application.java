package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Application")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ApplicationID")
    private Integer applicationId;

    @Column(name = "CandidateID", nullable = false)
    private Integer candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CandidateID", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "JobID", nullable = false)
    private Integer jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "JobID", insertable = false, updatable = false)
    private JobPost jobPost;

    @Column(name = "CVID", nullable = false)
    private Integer cvId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CVID", insertable = false, updatable = false)
    private CV cv;

    @Column(name = "AppliedDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime appliedDate = LocalDateTime.now();

    @Column(name = "CoverLetter", columnDefinition = "NVARCHAR(MAX)")
    private String coverLetter;

    @Column(name = "Status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(name = "Note", length = 500)
    private String note;

    // Relationships
    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<ApplicationStatusHistory> statusHistories = new HashSet<>();

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Interview> interviews = new HashSet<>();
}
