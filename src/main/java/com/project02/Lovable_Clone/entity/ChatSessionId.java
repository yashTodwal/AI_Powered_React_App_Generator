package com.project02.Lovable_Clone.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatSessionId implements Serializable{
    Long projectId;
    Long userId;
}
