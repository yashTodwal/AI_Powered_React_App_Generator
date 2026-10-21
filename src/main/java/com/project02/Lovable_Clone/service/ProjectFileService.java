package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.project.FileContentResponse;
import com.project02.Lovable_Clone.dto.project.FileNode;
import com.project02.Lovable_Clone.dto.project.FileTreeResponse;

import java.util.List;

public interface ProjectFileService {
    FileTreeResponse getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
