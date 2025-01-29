package org.acme.projectjobschedule.data;

import ai.timefold.solver.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.acme.projectjobschedule.domain.Allocation;
import org.acme.projectjobschedule.domain.ExecutionMode;
import org.acme.projectjobschedule.domain.Project;
import org.acme.projectjobschedule.domain.resource.Resource;
import org.acme.projectjobschedule.domain.ResourceRequirement;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;

public class JsonExporter {

    private Map<String, Object> jsonMap;
    private List<JsonAllocationList> jallocationList;
    private List<String> projectsPid;
    private List<String> resourcesRid;

    public JsonExporter(HardMediumSoftScore score, String ID, LocalDateTime StartDate, LocalDateTime EndDate, List<Project> projects, List<Resource> resources,
                        List<ResourceRequirement> requirementList,  List<Allocation> allocationList) {
        this.jallocationList = new ArrayList<>();
        this.projectsPid = new ArrayList<>();
        this.resourcesRid = new ArrayList<>();
        int hardScore = score.hardScore();
        for(Project project : projects){
            String originalPID = project.getPID(); // Короткая строка
            String trimmedPID = (originalPID.length() > 4)
                    ? originalPID.substring(4)
                    : originalPID;
            projectsPid.add(trimmedPID);
        }

        for(Resource resource : resources){
            resourcesRid.add(resource.getRID());
        }

        for(Allocation allocation : allocationList){
            JsonAllocationList jallocation = new JsonAllocationList(allocation.getId(), allocation.getProject().getPID(),
                    allocation.getJob().getJID(), StartDate, EndDate, allocation.getStartDate(),
                    allocation.getEndDate(),  allocation.getExecutionMode().getDuration(),requirementList,
                    allocation.getExecutionMode(), allocation.getPredecessorAllocations());
            jallocationList.add(jallocation);
        }

        this.jsonMap = new LinkedHashMap<>();
        jsonMap.put("ID", ID);
        jsonMap.put("HardConstraintsPenalty", score.hardScore());
        jsonMap.put("MediumConstraintsPenalty1", score.mediumScore());
        jsonMap.put("SoftConstraintsPenalty", score.softScore());
        jsonMap.put("Projects", projectsPid);
        jsonMap.put("Resources", resourcesRid);
        jsonMap.put( "AllocationList", jallocationList);
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

        public JsonAllocationList(String id, String pid, String jid, LocalDateTime startDate, LocalDateTime endDate, int allocStartDate,
                              int allocEndDate, int duration, List<ResourceRequirement> requirementsList,
                              ExecutionMode executionMode,
                              List<Allocation> predAllocationlist){
            int numericId = Integer.valueOf(id) + 1;
            this.id = "Allocation" + numericId;
            this.pid = (pid.length() > 4)
                    ? pid.substring(4)
                    : pid;
            this.jid = jid;
            this.startDate = String.valueOf(startDate.plusMinutes(allocStartDate));
            this.endDate = String.valueOf(endDate.plusMinutes(allocEndDate));
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
}