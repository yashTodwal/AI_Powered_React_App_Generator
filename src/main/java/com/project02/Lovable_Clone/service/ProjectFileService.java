package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.project.FileContentResponse;
import com.project02.Lovable_Clone.dto.project.FileNode;

import java.util.List;

public interface ProjectFileService {
    List<FileNode> getFileTree(Long projectId);

    FileContentResponse getFileContent(Long projectId, String path);

    void saveFile(Long projectId, String filePath, String fileContent);
}
