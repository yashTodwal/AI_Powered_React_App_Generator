package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.entity.Project;
import com.project02.Lovable_Clone.entity.ProjectFile;
import com.project02.Lovable_Clone.error.ResourceNotFoundException;
import com.project02.Lovable_Clone.repository.ProjectFileRepository;
import com.project02.Lovable_Clone.repository.ProjectRepository;
import com.project02.Lovable_Clone.service.ProjectTemplateService;
import io.minio.*;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectTemplateServiceImpl implements ProjectTemplateService {

    private final MinioClient minioClient;
    private final ProjectFileRepository projectFileRepository;
    private final ProjectRepository projectRepository;

    private static final String TEMPLATE_BUCKET = "starter-projects";
    private static final String TARGET_BUCKET = "projects";
    private static final String TEMPLATE_NAME = "project-companion-main/react-vite-tailwind-daisyui-starter";
    @Override
    public void initializeProjectFromTemplate(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> new ResourceNotFoundException("Project", projectId.toString()));
        try {
            Iterable<Result<Item>> results = minioClient.listObjects(
                    ListObjectsArgs.builder()
                            .bucket(TEMPLATE_BUCKET)
                            .prefix(TEMPLATE_NAME + "/")
                            .recursive(true)
                            .build()
            );

            List<ProjectFile> filesToSave = new ArrayList<>(); // for metadata in postgres db

            for (Result<Item> result : results) {
                Item item = result.get();
                String sourceKey = item.objectName();
                if (sourceKey.endsWith("/")) {
                    continue; // skip directory markers
                }
                String cleanPath = sourceKey.replaceFirst(TEMPLATE_NAME + "/", "");
                String destKey = projectId + "/" + cleanPath;
                // Essential workaround: Stream the file instead of using copyObject
                // to avoid the SeaweedFS DateTimeParseException bug
                try (InputStream stream = minioClient.getObject(
                        GetObjectArgs.builder().bucket(TEMPLATE_BUCKET).object(sourceKey).build())) {

                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(TARGET_BUCKET)
                                    .object(destKey)
                                    .stream(stream, -1, 10485760)
                                    .build()
                    );
                }

                ProjectFile pf = ProjectFile.builder()
                        .project(project)
                        .path(cleanPath)
                        .minioObjectKey(destKey)
                        .createdAt(Instant.now())
                        .updatedAt(Instant.now())
                        .build();

                filesToSave.add(pf);
            }

            projectFileRepository.saveAll(filesToSave);

        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize project from template", e);
        }
    }
}
