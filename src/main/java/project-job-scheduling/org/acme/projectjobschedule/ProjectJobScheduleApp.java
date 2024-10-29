package org.acme.projectjobschedule;

import ai.timefold.solver.core.api.solver.Solver;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
// import org.acme.projectjobschedule.domain.Allocation;
// import org.acme.projectjobschedule.domain.ExecutionMode;
import org.acme.projectjobschedule.domain.Job;
// import org.acme.projectjobschedule.domain.JobType;
// import org.acme.projectjobschedule.domain.Project;
import org.acme.projectjobschedule.domain.ProjectJobSchedule;
// import org.acme.projectjobschedule.domain.ResourceRequirement;
import org.acme.projectjobschedule.solver.ProjectJobSchedulingConstraintProvider;
import org.acme.projectjobschedule.rest.DemoDataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// import java.time.DayOfWeek;
import java.time.Duration;
// import java.time.LocalTime;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;
// import java.util.Map;
// import java.util.Objects;
// import java.util.stream.Collectors;

public class ProjectJobScheduleApp {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProjectJobScheduleApp.class);

    public enum DemoData {
        SMALL,
        LARGE
    }

    public static void main(String[] args) {
        SolverFactory<ProjectJobSchedule> solverFactory = SolverFactory.create(new SolverConfig()
                .withSolutionClass(ProjectJobSchedule.class)
                .withEntityClasses(Job.class)
                .withConstraintProviderClass(ProjectJobSchedulingConstraintProvider.class)
                // The solver runs only for 5 seconds on this small dataset.
                // It's recommended to run for at least 5 minutes ("5m") otherwise.
                .withTerminationSpentLimit(Duration.ofSeconds(5)));

        // Load the problem
        ProjectJobSchedule problem = DemoDataGenerator(DemoData.SMALL);

        // Solve the problem
        Solver<ProjectJobSchedule> solver = solverFactory.buildSolver();
        ProjectJobSchedule solution = solver.solve(problem);

        // Visualize the solution
       // printProjectJobSchedule(solution);
    }

}

/*rivate static void printProjectJobSchedule(ProjectJobSchedule projectJobSchedule) {
    LOGGER.info("");
    List<Allocation> alllocation = projectJobSchedule.getAllocations();
    List<Job> jobs = projectJobSchedule.getJobs();
    Map<Timeslot, Map<Room, List<Lesson>>> lessonMap = lessons.stream()
            .filter(lesson -> lesson.getTimeslot() != null && lesson.getRoom() != null)
            .collect(Collectors.groupingBy(Lesson::getTimeslot, Collectors.groupingBy(Lesson::getRoom)));
    LOGGER.info("|            | " + rooms.stream()
            .map(room -> String.format("%-10s", room.getName())).collect(Collectors.joining(" | ")) + " |");
    LOGGER.info("|" + "------------|".repeat(rooms.size() + 1));
    for (Timeslot timeslot : timeTable.getTimeslots()) {
        List<List<Lesson>> cells = rooms.stream()
                .map(room -> {
                    Map<Room, List<Lesson>> byRoomMap = lessonMap.get(timeslot);
                    if (byRoomMap == null) {
                        return Collections.<Lesson>emptyList();
                    }
                    List<Lesson> cellLessons = byRoomMap.get(room);
                    return Objects.requireNonNullElse(cellLessons, Collections.<Lesson>emptyList());
                }).toList();

        LOGGER.info("| " + String.format("%-10s",
                timeslot.getDayOfWeek().toString().substring(0, 3) + " " + timeslot.getStartTime()) + " | "
                + cells.stream().map(cellLessons -> String.format("%-10s",
                        cellLessons.stream().map(Lesson::getSubject).collect(Collectors.joining(", "))))
                .collect(Collectors.joining(" | "))
                + " |");
        LOGGER.info("|            | "
                + cells.stream().map(cellLessons -> String.format("%-10s",
                        cellLessons.stream().map(Lesson::getTeacher).collect(Collectors.joining(", "))))
                .collect(Collectors.joining(" | "))
                + " |");
        LOGGER.info("|            | "
                + cells.stream().map(cellLessons -> String.format("%-10s",
                        cellLessons.stream().map(Lesson::getStudentGroup).collect(Collectors.joining(", "))))
                .collect(Collectors.joining(" | "))
                + " |");
        LOGGER.info("|" + "------------|".repeat(rooms.size() + 1));
    }
    List<Lesson> unassignedLessons = lessons.stream()
            .filter(lesson -> lesson.getTimeslot() == null || lesson.getRoom() == null)
            .toList();
    if (!unassignedLessons.isEmpty()) {
        LOGGER.info("");
        LOGGER.info("Unassigned lessons");
        for (Lesson lesson : unassignedLessons) {
            LOGGER.info("  " + lesson.getSubject() + " - " + lesson.getTeacher() + " - " + lesson.getStudentGroup());
        }
    }
}

}
*/