package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "Education")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EducationID")
    private Integer educationId;

    @Column(name = "CandidateID", nullable = false)
    private Integer candidateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CandidateID", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @Column(name = "SchoolName", nullable = false, length = 200)
    private String schoolName;

    @Column(name = "Degree", length = 100)
    private String degree;

    @Column(name = "Major", length = 100)
    private String major;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;
}
