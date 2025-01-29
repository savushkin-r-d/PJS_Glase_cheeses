package org.acme.projectjobschedule.app;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.config.solver.SolverConfig;
import ai.timefold.solver.core.config.solver.termination.TerminationConfig;
import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.solver.ProjectJobSchedulingConstraintProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import org.acme.projectjobschedule.data.*;
import java.util.List;

public class ProjectJobScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectJobScheduleApp.class);

    public enum DemoData {
        SMALL,
        LARGE
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
         DemoDataGenerator demo_data = new DemoDataGenerator();
        ProjectJobSchedule problem1 = demo_data.generateDemoData();

        // Solve the problem
        Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
        ProjectJobSchedule solution = solver.solve(problem);

        HardMediumSoftScore score = problem.getScore();

        List<Allocation> allocations = solution.getAllocations();
        int hardScore = score.hardScore();
        // Вывод или обработка данных
        for (Allocation allocation : allocations) {
            System.out.println();
            System.out.println();
            System.out.println("Allocation id: " + allocation.getId());
            System.out.println("sourceAllocation id: " + allocation.getSourceAllocation().getId());
            System.out.println("sinkAllocation id: " + allocation.getSinkAllocation().getId());

            if (allocation.getPredecessorAllocations().isEmpty() && !allocation.getSuccessorAllocations().isEmpty()) {
                System.out.println("predecessorAllocations is empty!");
                System.out.print("successorAllocations id: ");
                for (Allocation successorAllocation : allocation.getSuccessorAllocations()) {
                    System.out.print(successorAllocation.getId() + ",");
                }
            }
            else if (!allocation.getPredecessorAllocations().isEmpty() && !allocation.getSuccessorAllocations().isEmpty()) {
                System.out.print("predecessorAllocations id: ");
                for (Allocation predeseccorAllocation : allocation.getPredecessorAllocations()) {
                    System.out.print(predeseccorAllocation.getId() + ",");
                }
                System.out.println();
                System.out.print("successorAllocations id: ");
                for (Allocation successorAllocation : allocation.getSuccessorAllocations()) {
                    System.out.print( successorAllocation.getId() + ",");
                }
            }
            else if (allocation.getPredecessorAllocations().isEmpty() && allocation.getSuccessorAllocations().isEmpty()) {
                System.out.println("predecessorAllocations is empty!");
                System.out.println("successorAllocations is empty!");
            } else if (!allocation.getPredecessorAllocations().isEmpty() && allocation.getSuccessorAllocations().isEmpty()) {
                System.out.print("predecessorAllocations id: ");
                for (Allocation predeseccorAllocation : allocation.getPredecessorAllocations()) {
                    System.out.print(predeseccorAllocation.getId() + ",");
                }
                System.out.println();
                System.out.println("successorAllocations is empty!");
            }
        }

        JsonExporter exporter = new JsonExporter(score, model.getID(),model.getStartDate(), model.getEndDate(), problem.getProjects(), problem.getResources(),problem.getResourceRequirements(),allocations);
        exporter.convertToJsonFile("src/main/resources/exportData.json");
    }


    }


