package org.acme.projectjobschedule.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import java.time.Duration;

import org.acme.projectjobschedule.data.*;
import org.acme.projectjobschedule.domain.*;

import ai.timefold.solver.core.api.score.ScoreExplanation;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;

import org.acme.projectjobschedule.solver.ProjectJobSchedulingConstraintProvider;

import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.Solver;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProjectJobScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectJobScheduleApp.class);

    public static void main(String[] args) throws FileNotFoundException {

        // Load the problem from JSON
        String filePath = "src/main/resources/exampleData.json"; // Путь к файлу JSON

        File importFile = new File(filePath);
        if(importFile.exists()) {
            DataModel model = new DataModel(filePath);
            model.readOperationHashMap();
            ProjectJobSchedule problem = model.generateProjectJobSchedule();
            SolverFactory<ProjectJobSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                    .withSolutionClass(ProjectJobSchedule.class)
                    .withEntityClasses(Allocation.class)
                    .withConstraintProviderClass(ProjectJobSchedulingConstraintProvider.class)
                    // The solver runs only for 5 seconds on this small dataset.
                    // It's recommended to run for at least 5 minutes ("5m") otherwise.
                    .withTerminationConfig(new TerminationConfig()
                            .withSpentLimit(Duration.ofSeconds(model.getTS())) // Максимум 5 минут
                            .withUnimprovedSpentLimit(Duration.ofSeconds(model.getUS())))); // Или 1 минута без улучшений

            // Solve the problem
            Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
            ProjectJobSchedule solution = solver.solve(problem);

            SolutionManager<ProjectJobSchedule, HardMediumSoftScore> solutionManager = SolutionManager.create(solverFactory);
            ScoreExplanation<ProjectJobSchedule, HardMediumSoftScore> scoreExplanation = solutionManager.explain(solution);

            HardMediumSoftScore score = problem.getScore();

            List<Allocation> allocations = solution.getAllocations();

            JsonExporter exporter = new JsonExporter(score, model.getID(), model.getStartDate(),
                    problem.getProjects(), problem.getResources(), problem.getResourceRequirements(),
                    allocations, scoreExplanation);
            exporter.convertToJsonFile("src/main/resources/exportData.json");

            ImportExecutionModesTable dbTable = new ImportExecutionModesTable();
        }

        else {
            LOGGER.info("Import file not found in directory src/main/resources/ !");
        }
    }
    }


