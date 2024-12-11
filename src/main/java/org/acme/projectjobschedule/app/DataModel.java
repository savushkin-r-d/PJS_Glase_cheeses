package org.acme.projectjobschedule.app;

import java.util.ArrayList;
import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonArray;

import org.acme.projectjobschedule.domain.ExecutionMode;
import org.acme.projectjobschedule.domain.Job;
import org.acme.projectjobschedule.domain.Project;
import org.acme.projectjobschedule.domain.ResourceRequirement;
import org.acme.projectjobschedule.domain.resource.GlobalResource;
import org.acme.projectjobschedule.domain.resource.LocalResource;
import org.acme.projectjobschedule.domain.resource.Resource;
import com.fasterxml.jackson.databind.JsonNode;
import org.acme.projectjobschedule.domain.ExecutionMode;
import com.fasterxml.jackson.databind.ObjectMapper;
public class DataModel {

    private String id;
    private String StartDate;
    private String EndDate;
    private String Termination;

    List<Project> projects = new ArrayList<Project>();
    List<Resource> resources = new ArrayList<Resource>();
    List<ExecutionMode> executionModeList = new ArrayList<ExecutionMode>();
    List<Job> jobs = new ArrayList<Job>();
    List<String> successorJobs = new ArrayList<String>();
    List<ExecutionMode> executionModes = new ArrayList<ExecutionMode>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.StartDate = EndDate;
    }

    public String getTermination() {
        return Termination;
    }

    public void setTermination(String Termination) {
        this.Termination = Termination;
    }

    public List<Project> getProjectList() {
        return projects;
    }

    public void setProjectList(JsonNode rootNode) {
        JsonNode projectListNode = rootNode.get("ProjectList");
        if (projectListNode.isArray()) {
            for (JsonNode projectNode : projectListNode) {
                Project project = new Project();
                project.setId(projectNode.get("PID").asText());
                project.setPriority(projectNode.get("Priority").asInt());
                project.setVb(projectNode.get("VB").asInt());
                project.setGtin(projectNode.get("GTIN").asText());
                project.setNp(projectNode.get("NP").asInt());
                this.projects.add(project);

                JsonNode executionModeListNode = projectNode.get("ExecutionModeList");
                if (executionModeListNode.isArray()) {
                    for (JsonNode executionModeNode : executionModeListNode) {
                        ExecutionMode executionMode = new ExecutionMode();
                        executionMode.setId(executionModeNode.get("JID").asText());
                        executionModeNode.get("Duration").asInt();

                        JsonNode resourceRequirementListNode = executionModeNode.get("ResourceRequirementList");
                        if (resourceRequirementListNode.isArray()) {
                            for (JsonNode resourceRequirementNode : resourceRequirementListNode) {
                                ResourceRequirement requirement = new ResourceRequirement();
                                requirement.setId(resourceRequirementNode.get("RID").asText());
                                requirement.setRequirement(resourceRequirementNode.get("Requirement").asInt());
                            }
                        }
                        this.executionModeList.add(executionMode);
                    }

                }
            }

        }
    }

    public List<Job> getJobList() {
        return jobs;
    }

    public void setJobList(JsonNode rootNode) {
        JsonNode jobListNode = rootNode.get("JobList");

        for (JsonNode jobNode : jobListNode) {
            Job job = new Job();
            job.setId(jobNode.get("JID").asText());
            this.jobs.add(job);
        }
    }

    public List<Resource> getResourceList() {
        return resources;
    }

    public void setResourceList(JsonNode rootNode) {
        JsonNode resourceListNode = rootNode.get("ResourceList");
        if (resourceListNode.isArray()) {
            for (JsonNode resourceNode : resourceListNode) {
                if (resourceNode.get("@type").asText().equals("local")) {
                    LocalResource localResource = new LocalResource();
                    localResource.setId(resourceNode.get("RID").asText());
                    localResource.setCapacity(resourceNode.get("Capacity").asInt());
                    localResource.setRenewable(resourceNode.get("Renewable").asBoolean());
                    // resource.setRestrictionList(resourceObject.getJsonArray("RestrictionList").getValuesAs(Object.class));
                    this.resources.add(localResource);

                } else {
                    GlobalResource globalResource = new GlobalResource();
                    globalResource.setId(resourceNode.get("RID").asText());
                    globalResource.setCapacity(resourceNode.get("Capacity").asInt());
                    //globalResource.setRenewable(resourceNode.get("Renewable").asBoolean());
                    // resource.setRestrictionList(resourceObject.getJsonArray("RestrictionList").getValuesAs(Object.class));
                    this.resources.add(globalResource);
                }
            }
        }

        public List<ExecutionMode> getExecutionModeList() {
            return executionModeList;
        }

    }
}
