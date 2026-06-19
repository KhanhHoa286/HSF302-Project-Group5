package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Recruiter")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Recruiter {
    @Id
    @Column(name = "RecruiterID")
    private Integer recruiterId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RecruiterID", insertable = false, updatable = false)
    private User user;

    @Column(name = "CompanyID", nullable = false)
    private Integer companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CompanyID", insertable = false, updatable = false)
    private Company company;

    // Relationships
    @OneToMany(mappedBy = "recruiter", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<JobPost> jobPosts = new HashSet<>();
}
