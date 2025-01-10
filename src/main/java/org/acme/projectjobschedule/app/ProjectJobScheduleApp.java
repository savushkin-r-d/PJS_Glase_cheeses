package org.acme.projectjobschedule.app;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.config.solver.SolverConfig;
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
        String filePath = "src/main/resources/data.json"; // Путь к файлу JSON


        DataModel model = new DataModel(filePath);
        model.readOperationHashMap();
        ProjectJobSchedule problem = model.generateProjectJobSchedule();
        // Solve the problem
        Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
        ProjectJobSchedule solution = solver.solve(problem);

        List<Allocation> allocations = solution.getAllocations();

// Вывод или обработка данных
        for (Allocation allocation : allocations) {
            System.out.println("ProjectCriticalPathEndDate: " + allocation.getProjectCriticalPathEndDate() +
                    ", ProjectDelay: " + allocation.getProjectDelay() +
                    ", JobType: " + allocation.getJobType() +
                    ", StartDate: " + allocation.getStartDate() +
                    ", EndDate: " + allocation.getEndDate());
    }
}
}
