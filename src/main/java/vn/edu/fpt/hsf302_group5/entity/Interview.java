package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import vn.edu.fpt.hsf302_group5.entity.enums.InterviewStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "Interview")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InterviewID")
    private Integer interviewId;

    @Column(name = "ApplicationID", nullable = false)
    private Integer applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApplicationID", insertable = false, updatable = false)
    private Application application;

    @Column(name = "InterviewDate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime interviewDate;

    @Column(name = "Location", length = 255)
    private String location;

    @Column(name = "MeetingLink", length = 500)
    private String meetingLink;

    @Column(name = "Note", length = 500)
    private String note;

    @Column(name = "CreatedAt", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "Status", length = 20)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private InterviewStatus status = InterviewStatus.SCHEDULED;
}
