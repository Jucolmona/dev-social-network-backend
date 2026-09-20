package com.codefactory.dev_social_network.proyectos.controller;

import com.codefactory.dev_social_network.proyectos.dto.CreateProjectRequest;
import com.codefactory.dev_social_network.proyectos.dto.ProjectResponse;
import com.codefactory.dev_social_network.proyectos.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
@Tag(
        name = "Projects",
        description = "Operations related to developer projects"
)
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Operation(
            summary = "Create a project",
            description = "Publishes a new project with its description, repository and technologies"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Project created successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProjectResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected server error"
            )
    })
    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody CreateProjectRequest request
    ) {
        ProjectResponse response = projectService.createProject(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}