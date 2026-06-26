package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "application_status_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "history_id")
    private Integer historyId;

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;

    @Column(name = "old_status", length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus oldStatus;

    @Column(name = "new_status", length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus newStatus;

    @Column(name = "changed_by", nullable = false)
    private Integer changedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "changed_by", insertable = false, updatable = false)
    private User user;

    @Column(name = "changed_at", nullable = false)
    @Builder.Default
    private LocalDateTime changedAt = LocalDateTime.now();
}

