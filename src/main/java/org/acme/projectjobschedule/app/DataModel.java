package org.acme.projectjobschedule.app;

import java.util.Collections;
import java.util.List;

import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.GlobalResource;
import org.acme.projectjobschedule.domain.resource.LocalResource;
import org.acme.projectjobschedule.domain.resource.Resource;
import com.fasterxml.jackson.databind.JsonNode;
import org.acme.projectjobschedule.domain.ExecutionMode;

public class DataModel {

    private String id;
    private String StartDate;
    private String EndDate;
    private String Termination;

    private List<Project> projects;
    private List<Resource> resources;
    private List<ExecutionMode> executionModeList;
    private List<Job> jobs;
    private List<String> successorJobs;
    private List<ResourceRequirement> resourceRequirementList;
    private List<String> RestrictionList;

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

    public List<Job> getJobList() {
        return jobs;
    }

    public List<Resource> getResourceList() {
        return resources;
    }

    public List<ExecutionMode> getExecutionModeList() {
        return executionModeList;
    }

    public void setProjectList(JsonNode rootNode) {
        JsonNode projectListNode = rootNode.get("ProjectList");
        if (projectListNode != null && projectListNode.isArray() && !projectListNode.isEmpty()) {
            for (JsonNode projectNode : projectListNode) {
                Project project = new Project();
                project.setId(projectNode.get("PID").asText());
                project.setPriority(projectNode.get("Priority").asInt());
                project.setVb(projectNode.get("VB").asInt());
                project.setGtin(projectNode.get("GTIN").asText());
                project.setNp(projectNode.get("NP").asInt());
                this.projects.add(project);

                JsonNode executionModeListNode = projectNode.get("ExecutionModeList");
                if (executionModeListNode != null && executionModeListNode.isArray() && !executionModeListNode.isEmpty()) {
                    for (JsonNode executionModeNode : executionModeListNode) {
                        ExecutionMode executionMode = new ExecutionMode();
                        executionMode.setId(executionModeNode.get("JID").asText());
                        executionModeNode.get("Duration").asInt();

                        JsonNode resourceRequirementListNode = executionModeNode.get("ResourceRequirementList");
                        if (resourceRequirementListNode != null && resourceRequirementListNode.isArray() && !resourceRequirementListNode.isEmpty()) {
                            for (JsonNode resourceRequirementNode : resourceRequirementListNode) {
                                ResourceRequirement requirement = new ResourceRequirement();
                                requirement.setId(resourceRequirementNode.get("RID").asText());
                                requirement.setRequirement(resourceRequirementNode.get("Requirement").asInt());
                                this.resourceRequirementList.add(requirement);
                            }

                        } else {
                            this.resourceRequirementList = Collections.emptyList();
                        }
                        this.executionModeList.add(executionMode);
                    }

                }
                this.executionModeList = Collections.emptyList();
            }

        }
    }

    public void setJobList(JsonNode rootNode) {
        JsonNode jobListNode = rootNode.get("JobList");
        if (jobListNode != null && jobListNode.isArray() && !jobListNode.isEmpty()) {
            for (JsonNode jobNode : jobListNode) {
                Job job = new Job();
                job.setId(jobNode.get("JID").asText());
                JsonNode successorListNode = jobNode.get("SuccessorList");
                if (successorListNode != null && successorListNode.isArray() && !successorListNode.isEmpty()) {
                    for (JsonNode successor : successorListNode) {
                        this.successorJobs.add(successor.asText());
                    }
                } else {
                    this.successorJobs = Collections.emptyList();
                }
                this.jobs.add(job);
            }

        }
    }


    public void setResourceList(JsonNode rootNode) {
        JsonNode resourceListNode = rootNode.get("ResourceList");
        if (resourceListNode != null && resourceListNode.isArray() && !resourceListNode.isEmpty()) {
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
    }

    public void setRestrictionList(JsonNode rootNode) {
        JsonNode jobListNode = rootNode.get("JobList");
        if (jobListNode != null && jobListNode.isArray() && !jobListNode.isEmpty()) {
            for (JsonNode jobNode : jobListNode) {
                JsonNode RestrictionLosttNode = jobNode.get("RestrictionList");
                if (RestrictionLosttNode != null && RestrictionLosttNode.isArray() && !RestrictionLosttNode.isEmpty()) {
                    for (JsonNode restriction : RestrictionLosttNode) {
                        this.RestrictionList.add(restriction.asText());
                    }
                } else {
                    this.RestrictionList= Collections.emptyList();
                }
            }

        }
    }
}

