package vn.edu.fpt.hsf302_group5.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SavedJobId implements Serializable {
    @Column(name = "CandidateID")
    private Integer candidateId;

    @Column(name = "JobID")
    private Integer jobId;
}
