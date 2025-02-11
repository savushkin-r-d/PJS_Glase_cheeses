package org.acme.projectjobschedule.data;

import ai.timefold.solver.core.api.score.ScoreExplanation;
import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import ai.timefold.solver.core.api.score.constraint.ConstraintMatch;
import ai.timefold.solver.core.api.score.constraint.Indictment;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.Resource;
import java.time.LocalDateTime;

public class JsonExporter {

    private Map<String, Object> jsonMap;
    private HardMediumSoftScore totalScore;

    public JsonExporter(HardMediumSoftScore score, String ID, LocalDateTime StartDate, List<Project> projects, List<Resource> resources,
                        List<ResourceRequirement> requirementList, List<Allocation> allocations, ScoreExplanation<ProjectJobSchedule, HardMediumSoftScore> scoreExplanation) {

        int hardScore = score.hardScore();
        List<String> projectsPid = getProjectsPid(projects);
        List<String> resourcesRid = getResourcesRid(resources);
        List<JsonAllocationList> jallocationList = getJallocationList(allocations, StartDate, requirementList);
        List<ResultAnalyze> jIndicments = getIndicments(scoreExplanation);
        this.jsonMap = new LinkedHashMap<>();

        jsonMap.put("ID", ID);
        jsonMap.put("HardConstraintsPenalty", score.hardScore());
        jsonMap.put("MediumConstraintsPenalty1", score.mediumScore());
        jsonMap.put("SoftConstraintsPenalty", score.softScore());
        jsonMap.put("Projects", projectsPid);
        jsonMap.put("Resources", resourcesRid);
        jsonMap.put("AllocationList", jallocationList);
        jsonMap.put("IndicmentsList", jIndicments);
        jsonMap.put("TotalMatch", totalScore);
    }

    public void convertToJsonFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        // objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, false);
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), jsonMap);
            System.out.println("JSON файл успешно создан: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> getResourcesRid(List<Resource> resources){
        List<String> resourcesRid = new ArrayList<>();
        for (Resource resource : resources) {
            resourcesRid.add(resource.getRID());
        }
         return resourcesRid;
    }
    private List<String> getProjectsPid(List<Project> projects){
        List<String> projectsPid = new ArrayList<>();
        for (Project project : projects) {
            String originalPID = project.getPID(); // Короткая строка
            String trimmedPID = (originalPID.length() > 4)
                    ? originalPID.substring(4)
                    : originalPID;
            projectsPid.add(trimmedPID);
        }
        return projectsPid;
    }

    private List<JsonAllocationList> getJallocationList(List<Allocation> allocations, LocalDateTime StartDate,
                                                        List<ResourceRequirement> requirementList ){
        List<JsonAllocationList> jallocationList = new ArrayList<>();
        for (Allocation allocation : allocations) {
            if (allocation.getJob().getJID().equals("SOURCE") || allocation.getJob().getJID().equals("SINK")) {
                continue;
            }
            JsonAllocationList jallocation = new JsonAllocationList(allocation.getId(), allocation.getProject().getPID(),
                    allocation.getJob().getJID(), StartDate, allocation.getStartDate(), allocation.getExecutionMode().getDuration(), requirementList,
                    allocation.getExecutionMode(), allocation.getPredecessorAllocations());
            jallocationList.add(jallocation);
        }
        return jallocationList;
    }

    private List<ResultAnalyze> getIndicments(ScoreExplanation<ProjectJobSchedule, HardMediumSoftScore> scoreExplanation) {

        Map<Object, Indictment<HardMediumSoftScore>> indictmentMap = scoreExplanation.getIndictmentMap();
        List<ResultAnalyze> indicmentsList = new ArrayList<>();
        this.totalScore = HardMediumSoftScore.ZERO;

        for (var entry : indictmentMap.entrySet()) {
            Object allocation = entry.getKey(); // Объект, например, Allocation-8
            Indictment<HardMediumSoftScore> indictment = entry.getValue(); // Информация об обвинениях
            int matchCount = indictment.getConstraintMatchSet().size();

            HardMediumSoftScore indictmentScore = indictment.getScore();
            totalScore = totalScore.add(indictmentScore);
            // Проходим по каждому нарушенному ограничению и штрафу
            for (ConstraintMatch<HardMediumSoftScore> constraintMatch : indictment.getConstraintMatchSet()) {

                indicmentsList.add(new ResultAnalyze(String.valueOf(allocation),String.valueOf(matchCount),constraintMatch.getConstraintRef().constraintName(),
                        String.valueOf(constraintMatch.getScore())));
            }
        }
        return indicmentsList;
    }

    @JsonPropertyOrder({ "ID", "PID", "JID", "StartDate", "EndDate", "Duration", "PredAllocationList" })
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)

    static class JsonAllocationList{

        // Формат даты и времени
        private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        private String id;
        private String pid;
        private String jid;
        private String startDate;
        private String endDate;
        private int duration;
        private List<String> resourceRequirementList;
        private List<String> predAllocationList;

        // Геттеры
        @JsonProperty("ID")
        public String getID() {
            return id;
        }

        @JsonProperty("PID")
        public String getPID() {
            return pid;
        }

        @JsonProperty("JID")
        public String getJID() {
            return jid;
        }

        @JsonProperty("StartDate")
        public String getStartDate() {
            return startDate;
        }

        @JsonProperty("EndDate")
        public String getEndDate() {
            return endDate;
        }

        @JsonProperty("Duration")
        public int getDuration() {
            return duration;
        }

        @JsonProperty("ResourceRequirementList")
        public List<String> getResourceRequirementList() {
            return resourceRequirementList;
        }

        @JsonProperty("PredAllocationList")
        public List<String> getPredAllocationList() {
            return predAllocationList;
        }

        public JsonAllocationList(String id, String pid, String jid, LocalDateTime startDate, int allocStartDate,
                              int duration, List<ResourceRequirement> requirementsList,
                              ExecutionMode executionMode,
                              List<Allocation> predAllocationlist){
            int numericId = Integer.valueOf(id) + 1;
            this.id = "Allocation" + numericId;
            this.pid = (pid.length() > 4)
                    ? pid.substring(4)
                    : pid;
            this.jid = jid;
            this.startDate = String.valueOf(startDate.plusMinutes(allocStartDate).format(formatter));
            this.endDate = String.valueOf(startDate.plusMinutes(allocStartDate + duration).format(formatter));
            this.duration = duration;
            this.resourceRequirementList = new ArrayList<>();
            this.predAllocationList = new ArrayList<>();
            for(ResourceRequirement requirement : requirementsList){
               if(requirement.getExecutionMode().equals(executionMode)){
                   resourceRequirementList.add(requirement.getRID());
               }
            }
            for(Allocation predAllocation : predAllocationlist){
                int numericAllocId = Integer.valueOf(predAllocation.getId()) + 1;
                predAllocationList.add("Allocation" + numericAllocId );
}
        }
    }
    @JsonPropertyOrder({ "Allocation", "MatchCount", "Constraint", "Penalty" })
    @JsonFormat(shape = JsonFormat.Shape.OBJECT)
    static class ResultAnalyze{

        private String allocationNum;
        private String constraintName;
        private String penaltyScore;
        private String matchCount;

        @JsonProperty("Allocation")
        public String getAllocationNum() {
            return allocationNum;
        }

        @JsonProperty("MatchCount")
        public String getMatchCount() {
            return matchCount;
        }
        @JsonProperty("Constraint")
        public String getConstraintName() {
            return constraintName;
        }
        @JsonProperty("Penalty")
        public String getPenaltyScore() {
            return penaltyScore;
        }

        public ResultAnalyze(String  allocationNum, String matchCount, String constraintName,String penaltyScore){
            this.allocationNum = allocationNum;
            this.matchCount = matchCount;
            this.constraintName = constraintName;
            this.penaltyScore = penaltyScore;
        }
    }
}