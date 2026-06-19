package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CandidateSkill")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateSkill {
    @EmbeddedId
    private CandidateSkillId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CandidateID", insertable = false, updatable = false)
    private CandidateProfile candidateProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SkillID", insertable = false, updatable = false)
    private Skill skill;
}
