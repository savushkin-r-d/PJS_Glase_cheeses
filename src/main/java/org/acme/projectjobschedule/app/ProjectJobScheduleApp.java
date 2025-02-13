package org.acme.projectjobschedule.app;

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

public class ProjectJobScheduleApp {

    public static void main(String[] args) {

        // Load the problem from JSON
        String filePath = "src/main/resources/importData.json"; // Путь к файлу JSON
        
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

        JsonExporter exporter = new JsonExporter(score, model.getID(),model.getStartDate(),
                problem.getProjects(), problem.getResources(),problem.getResourceRequirements(),
                allocations,scoreExplanation);
        exporter.convertToJsonFile("src/main/resources/exportData.json");

    }
    }


