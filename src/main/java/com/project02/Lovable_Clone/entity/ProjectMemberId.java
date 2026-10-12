package com.project02.Lovable_Clone.entity;

import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMemberId {
    Long projectId;
    Long userId;
}
