package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "JobPost")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JobID")
    private Integer jobId;

    @Column(name = "RecruiterID", nullable = false)
    private Integer recruiterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecruiterID", insertable = false, updatable = false)
    private Recruiter recruiter;

    @Column(name = "IndustryID", nullable = false)
    private Integer industryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IndustryID", insertable = false, updatable = false)
    private Industry industry;

    @Column(name = "ApprovedBy")
    private Integer approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApprovedBy", insertable = false, updatable = false)
    private User approver;

    @Column(name = "JobLevel", length = 30)
    private String jobLevel;

    @Column(name = "Vacancies")
    @Builder.Default
    private Integer vacancies = 1;

    @Column(name = "Title", nullable = false, length = 200)
    private String title;

    @Column(name = "Description", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String description;

    @Column(name = "Requirement", columnDefinition = "NVARCHAR(MAX)")
    private String requirement;

    @Column(name = "Location", length = 200)
    private String location;

    @Column(name = "SalaryMin", precision = 18, scale = 2)
    private BigDecimal salaryMin;

    @Column(name = "SalaryMax", precision = 18, scale = 2)
    private BigDecimal salaryMax;

    @Column(name = "EmploymentType", length = 30)
    private String employmentType;

    @Column(name = "Status", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatus status = JobStatus.PENDING;

    @Column(name = "PostedDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime postedDate = LocalDateTime.now();

    @Column(name = "ExpiredDate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime expiredDate;

    @Column(name = "ApprovedDate")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime approvedDate;

    @Column(name = "AdminComment", length = 500)
    private String adminComment;

    // Relationships
    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<SavedJob> savedJobs = new HashSet<>();

    @OneToMany(mappedBy = "jobPost", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();
}
