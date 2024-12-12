package org.acme.projectjobschedule.app;

import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.Resource;
import org.acme.projectjobschedule.app.DataModel;
import java.io.File;
import java.io.IOException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonImporter {
    private String filepath;

    public String getFilepath(){
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public JsonImporter(String filePath) {
       this.filepath=filePath;
    }

    public JsonImporter(){

    }

    public DataModel loadFromFile(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        DataModel dataModel = new DataModel();
        try  {
            // Читаем JSON-файл в JsonNode
            JsonNode rootNode = objectMapper.readTree(new File(filePath));

            // Инициализация объекта DataModel
            dataModel.setJobList(rootNode);
            dataModel.setProjectList(rootNode);
            dataModel.setResourceList(rootNode);
            dataModel.setRestrictionList(rootNode);
            dataModel.setId(rootNode.get("ID").asText());
            dataModel.setStartDate(rootNode.get("StartDate").asText());
            dataModel.setEndDate(rootNode.get("EndDate").asText());
            dataModel.setTermination(rootNode.get("Termination").asText());

        } catch (IOException e) {
            e.printStackTrace();

        }
        return dataModel;
    }

    public void printDataObject(DataModel model){
        if (model != null) {
            System.out.println("ID: " + model.getId());
            System.out.println("StartDate: " + model.getStartDate());
            System.out.println("EndDate: " + model.getEndDate());
            System.out.println("Termination: " + model.getTermination());

            System.out.println("ResourceList:");
            for (Resource resource : model.getResourceList()) {
                System.out.println("  RID: " + resource.getId());
                System.out.println("  Capacity: " + resource.getCapacity());
                System.out.println("  Renewable: " + resource.isRenewable());
                //  System.out.println("  RestrictionList: " + resource.getRestrictionList());
            }

            System.out.println("JobList:");
            for (Job job : model.getJobList()) {
                System.out.println("  JID: " + job.getId());
                //System.out.println("  SuccessorList: " + job.getSuccessorList());
                System.out.println("  ExecutionModeList:");
                for (ExecutionMode executionMode : job.getExecutionModes()) {
                    System.out.println("    JID: " + executionMode.getId());
                    System.out.println("    Duration: " + executionMode.getDuration());

                    System.out.println("    ResourceRequirementList:");
                    for (ResourceRequirement resourceRequirement : executionMode.getResourceRequirements()) {
                        System.out.println("      RID: " + resourceRequirement.getId());
                        System.out.println("      Requirement: " + resourceRequirement.getRequirement());
                    }
                }
            }

            System.out.println("ProjectList:");
            for (Project project : model.getProjectList()) {
                System.out.println("  PID: " + project.getId());
                System.out.println("  Priority: " + project.getPriority());
                System.out.println("  VB: " + project.getVb());
                System.out.println("  GTIN: " + project.getGtin());
                System.out.println("  NP: " + project.getNp());

            }

        } else {
            System.out.println("DataModel is not loaded.");
        }

    }
}