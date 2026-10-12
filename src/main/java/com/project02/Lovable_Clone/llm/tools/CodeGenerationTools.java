package com.project02.Lovable_Clone.llm.tools;

import com.project02.Lovable_Clone.service.ProjectFileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CodeGenerationTools {
    private final ProjectFileService projectFileService;
    private final Long projectId;

    @Tool(name = "read_files",
    description = "Read the content of files. Only input the file names present inside the FILE_TREE. DO NOT input any path which is not present under the FILE_TREE.")
    public List<String> readFiles(
            @ToolParam(description = "List of relative paths (e.g., 'src/App.tsx, src/main.tsx')")
            String paths
    ){
        List<String> result = new ArrayList<>();
        // 1. Manually clean any brackets or quotes the LLM accidentally includes
        String cleanInput = paths.replace("[", "")
                .replace("]", "")
                .replace("\"", "")
                .replace("'", "");
        // 2. Safely split into a list
        List<String> parsedPaths = Arrays.stream(cleanInput.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
        for(String path: parsedPaths){
            String cleanPath = path.startsWith("/") ? path.substring(1):path;
            log.info("Requested file: {}",cleanPath);
            try{
                String content = projectFileService.getFileContent(projectId,cleanPath).content();
                result.add(String.format(
                        "--- START OF FILE: %s ---\n%s\n---END OF FILE---",
                        cleanPath,content
                ));
            } catch (Exception e) {
                log.error("Error reading file: {}", cleanPath, e);
                result.add(String.format("--- ERROR READING FILE: %s ---\nFile not found or unreadable.", cleanPath));
            }

        }
        return result;

    }
}
