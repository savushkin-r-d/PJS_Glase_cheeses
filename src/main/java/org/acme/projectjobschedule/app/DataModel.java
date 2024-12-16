package org.acme.projectjobschedule.app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.GlobalResource;
import org.acme.projectjobschedule.domain.resource.LocalResource;
import org.acme.projectjobschedule.domain.resource.Resource;
import org.acme.projectjobschedule.domain.ExecutionMode;

public class DataModel extends JsonImporter {

    private String ID;
    private String StartDate;
    private String EndDate;
    private String Termination;

    private List<Project> projects;
    private List<Resource> resources;
    private List<ExecutionMode> executionModeList;
    private List<Job> jobs;
    private List<String> successorJobsList;
    private List<ResourceRequirement> resourceRequirementList;
    private List<String> RestrictionList;
    private List<Resource> ResourceList;


    public DataModel(String filepath) {
        super(filepath);
    }
    //private final Map<String, Object> jsonMap;

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

    public void printProjects() {
        readOperationHashMap();
        initProjectList();
        initResourceRequirementList();
        initJobList();
        initBase();
        System.out.println("ID:" + this.ID);
        System.out.println("StartDate:" + this.StartDate);
        System.out.println("EndDate:" + this.EndDate);
        System.out.println("Termination:" + this.Termination);

        System.out.println("ResourceList:");

        if (this.ResourceList.isEmpty()){
            for (Resource resource : ResourceList){
                System.out.println("RID:" + resource.getId());
                System.out.println("Capacity:" + resource.getCapacity());
                System.out.println("Renewable:" + resource.isRenewable());
                System.out.println("RestrictionList:");
                for (String restriction : this.RestrictionList){
                    System.out.println(restriction);
                }
            }
        }

        System.out.println();
        System.out.println("JobList:");
        if (this.jobs.isEmpty()){
            for (Job job : this.jobs){
                System.out.println("JID:" + job.getId());
                System.out.println("SuccessorList:");
                for (String successorJob : this.successorJobsList){
                    System.out.print(successorJob + ",");
                }
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("ProjectList:");
        if (this.projects.isEmpty()) {
            System.out.println("ProjectList is empty");
        } else {
            for (Project project : this.projects) {
                System.out.println("PID:" + project.getId());
                System.out.println("Priority:" + project.getPriority());
                System.out.println("VB:" + project.getVb());
                System.out.println("GTIN:" + project.getGtin());
                System.out.println("NP:" + project.getNp());
                if(this.executionModeList.isEmpty()){
                    System.out.println("ExecutionModetList is empty");
                }
                else {
                    System.out.println("ExecutionModeList:");
                    for (ExecutionMode executionMode : this.executionModeList){
                        System.out.println("JID:" + executionMode.getId() );
                        System.out.println("ResourceRequirementList:");
                        for (ResourceRequirement resourceRequirement : this.resourceRequirementList){
                            System.out.print("RID:" +  resourceRequirement.getId());
                            System.out.print("Requirement:" +  resourceRequirement.getRequirement());
                        }
                        System.out.println();
                    }
                    System.out.println();
                }
            }
        }
        System.out.println();
    }


    private void initProjectList() {
        List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
        this.projects = new ArrayList<>();
        this.executionModeList = new ArrayList<>();
        for (Map<String, Object> jsonProject : jsonProjects) {
            Project project = new Project();
            String id = (String) jsonProject.get("PID");
            project.setId(id);
            int priority = (int) jsonProject.get("Priority");
            project.setPriority(priority);
            int vb = (int) jsonProject.get("VB");
            project.setVb(vb);
            String gtin = (String) jsonProject.get("GTIN");
            project.setGtin(gtin);
            int np = (int) jsonProject.get("NP");
            project.setNp(np);

                List<Map<String, Object>> jsonExecutionModeList = (List<Map<String, Object>>) jsonProject.get("ExecutionModeList");
                for (Map<String, Object> jsonExecutionMode : jsonExecutionModeList) {
                    ExecutionMode executionMode = new ExecutionMode();
                    String jid = (String) jsonExecutionMode.get("JID");
                    executionMode.setId(jid);
                    int duration = (int) jsonExecutionMode.get("Duration");
                    executionMode.setDuration(duration);
                    this.executionModeList.add(executionMode);
                }
            this.projects.add(project);
            }
        if (this.projects == null) {
            this.projects = Collections.emptyList();
        }
        if (this.executionModeList == null) {
            this.resourceRequirementList = Collections.emptyList();
        }
        }



   /* private void initExecutionModeList() {
        List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
        this.executionModeList = new ArrayList<>();
        for (Map<String, Object> jsonProject : jsonProjects) {
            List<Map<String, Object>> jsonExecutionModeList = (List<Map<String, Object>>) jsonProject.get("ExecutionModeList");
            for (Map<String, Object> jsonExecutionMode : jsonExecutionModeList) {
                ExecutionMode executionMode = new ExecutionMode();
                String jid = (String) jsonExecutionMode.get("JID");
                executionMode.setId(jid);
                int duration = (int) jsonExecutionMode.get("Duration");
                executionMode.setDuration(duration);
                this.executionModeList.add(executionMode);
            }
            if (this.executionModeList == null) {
                this.resourceRequirementList = Collections.emptyList();
            }
        }
    }*/

    private void initResourceRequirementList() {
        List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
        for (Map<String, Object> jsonProject : jsonProjects) {
            List<Map<String, Object>> jsonExecutionModeList = (List<Map<String, Object>>) jsonProject.get("ExecutionModeList");
            this.executionModeList = new ArrayList<>();

            for (Map<String, Object> jsonExecutionMode : jsonExecutionModeList) {
                List<Map<String, Object>> jsonResourceRequirementList = (List<Map<String, Object>>) jsonExecutionMode.get("ResourceRequirementList");
                this.resourceRequirementList = new ArrayList<>();
                for (Map<String, Object> jsonResourceRequirement : jsonResourceRequirementList) {
                    ResourceRequirement resourceRequirement = new ResourceRequirement();
                    String rid = (String) jsonResourceRequirement.get("RID");
                    resourceRequirement.setId(rid);
                    int requirement = (int) jsonResourceRequirement.get("Requirement");
                    resourceRequirement.setRequirement(requirement);
                    this.resourceRequirementList.add(resourceRequirement);

                }
                if (this.resourceRequirementList == null) {
                    this.resourceRequirementList = Collections.emptyList();
                }
            }
        }
    }

    private void initJobList() {
        List<Map<String, Object>> jsonJobs = (List<Map<String, Object>>) jsonMap.get("JobList");
        this.jobs = new ArrayList<>();
        for (Map<String, Object> jsonJob : jsonJobs) {
            Job job = new Job();
            String id = (String) jsonJob.get("JID");
            job.setId(id);
            List<String> successorList = (List<String>) jsonJob.get("SuccessorList");
            if (successorList == null) {
                this.successorJobsList = successorList;
            } else {
                this.successorJobsList = Collections.emptyList();
            }
            this.jobs.add(job);
        }
        if (this.jobs == null) {
            this.jobs = Collections.emptyList();
        }

    }

    private void initBase() {
        this.ID = (String) jsonMap.get("ID");
        this.StartDate = (String) jsonMap.get("StartDate");
        this.EndDate = (String) jsonMap.get("EndDate");
        this.Termination = (String) jsonMap.get("Termination");

        List<Map<String, Object>> jsonResourceList = (List<Map<String, Object>>) jsonMap.get("ResourceList");
        for (Map<String, Object> jsonResource : jsonResourceList) {

            this.ResourceList = new ArrayList<>();
            if (jsonResource.get("@type").equals("global")) {

                GlobalResource globalResource = new GlobalResource();
                String rid = (String) jsonResource.get("RID");
                globalResource.setId(rid);
                int capacity = (int) jsonResource.get("Capacity");
                List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
                if (RestrictionList == null) {
                    this.RestrictionList = restrictionList;
                } else {
                    this.RestrictionList = Collections.emptyList();
                }
            } else if (jsonResource.get("@type").equals("local")) {
                LocalResource localResource = new LocalResource();
                String rid = (String) jsonResource.get("RID");
                localResource.setId(rid);
                int capacity = (int) jsonResource.get("Capacity");
                boolean renewable = (boolean) jsonResource.get("Renewable");
                List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
                if (RestrictionList == null) {
                    this.RestrictionList = restrictionList;
                } else {
                    this.RestrictionList = Collections.emptyList();
                }
            }
        }
        if(this.ResourceList==null){
            this.ResourceList= Collections.emptyList();
        }
    }
}



