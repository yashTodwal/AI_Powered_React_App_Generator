package com.project02.Lovable_Clone.dto.project;

import java.time.Instant;

public record FileNode(
        String path
) {
    @Override
    public String toString() {
        return path;
    }
}
