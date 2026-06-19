package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.ApplicationStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "ApplicationStatusHistory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HistoryID")
    private Integer historyId;

    @Column(name = "ApplicationID", nullable = false)
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApplicationID", insertable = false, updatable = false)
    private Application application;

    @Column(name = "OldStatus", length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus oldStatus;

    @Column(name = "NewStatus", length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus newStatus;

    @Column(name = "ChangedBy", nullable = false)
    private Integer changedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChangedBy", insertable = false, updatable = false)
    private User user;

    @Column(name = "ChangedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime changedAt = LocalDateTime.now();
}
