package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_job")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavedJob {
    @EmbeddedId
    private SavedJobId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", insertable = false, updatable = false)
    private JobPost jobPost;

    @Column(name = "saved_at", nullable = false)
    @Builder.Default
    private LocalDateTime savedAt = LocalDateTime.now();
}

