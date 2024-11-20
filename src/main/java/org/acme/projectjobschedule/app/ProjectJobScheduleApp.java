package org.acme.projectjobschedule.app;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import org.acme.projectjobschedule.domain.Allocation;
import org.acme.projectjobschedule.domain.Job;
import org.acme.projectjobschedule.domain.Project;
import org.acme.projectjobschedule.domain.ProjectJobSchedule;
import org.acme.projectjobschedule.solver.ProjectJobSchedulingConstraintProvider;
import org.acme.projectjobschedule.data.DemoDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectJobScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectJobScheduleApp.class);

    public enum DemoData {
        SMALL,
        LARGE
    }

    public static void main(String[] args) {

        SolverFactory<ProjectJobSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(ProjectJobSchedule.class)
                .withEntityClasses(Allocation.class)
                .withConstraintProviderClass(ProjectJobSchedulingConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

        // Load the problem
     //   DemoDataGenerator demo_data = new DemoDataGenerator();
      //  ProjectJobSchedule problem = demo_data.generateDemoData();

        // Load the problem from JSON
        String relativePath = "src/main/java/org/acme/projectjobschedule/data/data.json";
        ProjectJobSchedule problem = importFromJson(relativePath);

        if (problem != null) {
            System.out.println("Projects: " + problem.getProjects());
            System.out.println("Resources: " + problem.getResources());
            System.out.println("Jobs: " + problem.getJobs());
            System.out.println("Allocations: " + problem.getAllocations());
        }
        // Solve the problem
        Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
        ProjectJobSchedule solution = solver.solve(problem);

        // Visualize the solution
       printProjectJobSchedule(solution);

    }

    public static ProjectJobSchedule importFromJson(String relativePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Path path = Paths.get(relativePath).toAbsolutePath();
            return objectMapper.readValue(path.toFile(), ProjectJobSchedule.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void  printProjectJobSchedule(ProjectJobSchedule schedule){
            LOGGER.info("");
            List<Project> projects = schedule.getProjects();
            List<Job> jobs = schedule.getJobs();

            Map<Project, List<Job>> projectJobMap = jobs.stream()
                    .filter(job -> job.getProject() != null)
                    .collect(Collectors.groupingBy(Job::getProject));

            LOGGER.info("| Project id |                   Project                     | Job type   |");
            LOGGER.info("|" + "------------|-----------------------------------------------|------------|");

            for (Project project : projects) {
                List<Job> projectJobs = projectJobMap.getOrDefault(project, Collections.emptyList());

                if (projectJobs.isEmpty()) {
                    LOGGER.info("| " + String.format("%-11s", project.getReleaseDate()) + " | "
                            + "No scheduled jobs".formatted() + " |");
                    continue;
                }

                for (Job job : projectJobs) {
                    LOGGER.info("| " + String.format("%-10s", project.getId()) + " | "
                            + String.format("%-11s", job.getProject()) + " | "
                            + String.format("%-10s", job.getJobType()) + " | ");
                }
                LOGGER.info("|" + "------------|-----------------------------------------------|------------|");
            }

            List<Job> unassignedJobs = jobs.stream()
                    .filter(job -> job.getProject() == null || job.getJobType() == null)
                    .toList();

            if (!unassignedJobs.isEmpty()) {
                LOGGER.info("");
                LOGGER.info("Unassigned jobs:");
                for (Job job : unassignedJobs) {
                    LOGGER.info("  " + job.getProject() + " - Job type: " + job.getJobType());
                }
            }
        }
    }




