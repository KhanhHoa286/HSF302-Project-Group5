package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "CV")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CVID")
    private Integer cvId;

    @Column(name = "CandidateID", nullable = false)
    private Integer candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CandidateID", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "CVName", nullable = false, length = 100)
    private String cvName;

    @Column(name = "FileName", nullable = false, length = 255)
    private String fileName;

    @Column(name = "FileUrl", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "UploadedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();

    // Relationships
    @OneToMany(mappedBy = "cv", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<Application> applications = new HashSet<>();
}
