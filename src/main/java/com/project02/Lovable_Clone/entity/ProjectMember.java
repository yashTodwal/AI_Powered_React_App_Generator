package com.project02.Lovable_Clone.entity;

import com.project02.Lovable_Clone.enums.ProjectRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "project_member")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProjectMember {

    @EmbeddedId
    ProjectMemberId Id;

    @ManyToOne
    @MapsId("projectId")
    Project project;

    @ManyToOne
    @MapsId("userId")
    User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ProjectRole projectRole;

    Instant invitedAt;
    Instant acceptedAt;
}
