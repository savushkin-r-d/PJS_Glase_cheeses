package org.acme.projectjobschedule.app;
import java.util.List;

import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.Resource;
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
}

