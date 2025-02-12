package org.acme.projectjobschedule.app;

import java.util.List;

import java.io.*;

import java.time.Duration;

import org.acme.projectjobschedule.data.*;
import org.acme.projectjobschedule.domain.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ai.timefold.solver.core.api.score.ScoreExplanation;
import ai.timefold.solver.core.api.score.analysis.ScoreAnalysis;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;

import org.acme.projectjobschedule.solver.ProjectJobSchedulingConstraintProvider;

import ai.timefold.solver.core.api.solver.SolutionManager;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.Solver;

import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;

public class ProjectJobScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectJobScheduleApp.class);

    public enum DemoData {
        SMALL,
        LARGE
    }

    private static void printScoreAnalysis(ScoreAnalysis<HardSoftScore> scoreAnalysis) {
        System.out.println("Score analysis (" + scoreAnalysis.score() + ")");

        for (var entry : scoreAnalysis.constraintMap().entrySet()) {
            var constraintId = entry.getKey();
            var matchTotal = entry.getValue();
            System.out.println("constraintId:" +
                    constraintId);
            System.out.println("constraintName:" + matchTotal.constraintName());
            System.out.println("matchCount:" + matchTotal.matchCount() );
            System.out.println("Totalscore:" + matchTotal.score());
            System.out.println("Totalweight:" + matchTotal.weight());
            System.out.println();
        }
    }

    public static void main(String[] args) {

        // Load the problem from JSON
        String filePath = "src/main/resources/importData.json"; // Путь к файлу JSON
        
        DataModel model = new DataModel(filePath);
        model.readOperationHashMap();
        ProjectJobSchedule problem = model.generateProjectJobSchedule();
        int temination;
        SolverFactory<ProjectJobSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(ProjectJobSchedule.class)
                .withEntityClasses(Allocation.class)
                .withConstraintProviderClass(ProjectJobSchedulingConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationConfig(new TerminationConfig()
                        .withSpentLimit(Duration.ofSeconds(model.getTS())) // Максимум 5 минут
                        .withUnimprovedSpentLimit(Duration.ofSeconds(model.getUS())))); // Или 1 минута без улучшений

        // Load the problem
        //DemoDataGenerator demo_data = new DemoDataGenerator();
       // ProjectJobSchedule problem1 = demo_data.generateDemoData();

        // Solve the problem
        Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
        ProjectJobSchedule solution = solver.solve(problem);

        SolutionManager<ProjectJobSchedule, HardMediumSoftScore> solutionManager = SolutionManager.create(solverFactory);
        ScoreExplanation<ProjectJobSchedule, HardMediumSoftScore> scoreExplanation = solutionManager.explain(solution);

        ScoreAnalysis<HardMediumSoftScore> scoreAnalysis = solutionManager.analyze(solution);

        HardMediumSoftScore score = problem.getScore();

        List<Allocation> allocations = solution.getAllocations();
        int hardScore = score.hardScore();
        // Вывод или обработка данных

        JsonExporter exporter = new JsonExporter(score, model.getID(),model.getStartDate(),
                problem.getProjects(), problem.getResources(),problem.getResourceRequirements(),
                allocations,scoreExplanation);
        exporter.convertToJsonFile("src/main/resources/exportData.json");

        try(FileWriter writer = new FileWriter("src/main/resources/score_explain.txt", false))
        {
            // запись всей строки
            writer.write(String.valueOf(solutionManager.explain(solution)));
            // запись по символам
            writer.append('\n');
            writer.append('E');

            writer.flush();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }
    }
    }


