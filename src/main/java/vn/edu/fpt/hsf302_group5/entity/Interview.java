package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.InterviewStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "interview_id")
    private Integer interviewId;

    @Column(name = "application_id", nullable = false)
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", insertable = false, updatable = false)
    private Application application;

    @Column(name = "interview_date", nullable = false)
    private LocalDateTime interviewDate;

    @Column(name = "location", length = 255)
    private String location;

    @Column(name = "meeting_link", length = 500)
    private String meetingLink;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;
}

