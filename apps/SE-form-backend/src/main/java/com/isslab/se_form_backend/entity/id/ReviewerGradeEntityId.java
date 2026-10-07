package com.isslab.se_form_backend.entity.id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerGradeEntityId implements Serializable {
    private String reviewerId;
    private String presenterId;
    private String week;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReviewerGradeEntityId that)) return false;
        return Objects.equals(reviewerId, that.reviewerId) &&
                Objects.equals(presenterId, that.presenterId) &&
                Objects.equals(week, that.week);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reviewerId, presenterId, week);
    }
}
