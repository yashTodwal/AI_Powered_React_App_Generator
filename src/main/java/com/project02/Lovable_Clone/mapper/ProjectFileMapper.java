package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.project.FileNode;
import com.project02.Lovable_Clone.entity.ProjectFile;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel ="spring")
public interface ProjectFileMapper {
    List<FileNode> toListOfFileNode(List<ProjectFile> projectFileList);
}
