package org.acme.projectjobschedule.app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.GlobalResource;
import org.acme.projectjobschedule.domain.resource.LocalResource;
import org.acme.projectjobschedule.domain.resource.Resource;
import org.acme.projectjobschedule.domain.ExecutionMode;

import static org.acme.projectjobschedule.domain.JobType.*;

public class DataModel extends JsonImporter {

    private String ID;
    private String StartDate;
    private String EndDate;
    private String Termination;

    private List<Resource> resources;
    private List <ExecutionMode> executionModes_fromJson;
    private Map<String, List<String>> successorJobMap;
    private List<List<ResourceRequirement>> resourceRequirementList;
    private List<String> RestrictionList;
    private List<Resource> ResourceList;


    public DataModel(String filepath) {
        super(filepath);
    }

    public List<Resource> getResourceList() {
        return resources;
    }

    public ProjectJobSchedule generateProjectJobSchedule(){
        ProjectJobSchedule projectJobSchedule = new ProjectJobSchedule();

        // Projects
        List<Project> projects = initProject();
        List<Resource> resources1 = initResource();
        List<Job> jobs1  = initJobList();
        List<Job> jobs = generateJobs(jobs1, projects, resources1);
        generateExecutionMode(jobs, projects.size());
        projectJobSchedule.setProjects(projects);
        projectJobSchedule.setResources(resources1);
        projectJobSchedule.setJobs(jobs);
        return projectJobSchedule;

    }

    private List<Job> generateJobs(List<Job> jobsFromJson, List<Project> projects, List<Resource> resources){
        int jobsSize = jobsFromJson.size();
        List<Job> jobs = new ArrayList<>(jobsSize*projects.size());
        int jobsCountPerProject = jobsSize;
        int countJob = 0;
        for (Project project : projects) {
            // Generate the job list
            List<Job> jobsPerProject = new ArrayList<>(jobsCountPerProject);
            for(Job json_job : jobsFromJson){
                jobsPerProject.add(new Job(String.valueOf(countJob++), project, json_job.getJobType()));
            }
            // Add all jobs of the given project

            jobs.addAll(jobsPerProject);
        }
        return jobs;
    }

    private void generateExecutionMode(List<Job> jobs, int projectsSize){
        List<ExecutionMode> executionModeList = new ArrayList<>();

            for (int i = 0; i < projectsSize; ++i) {
                String id = String.valueOf(i);
                List<ExecutionMode> executionModeSink = new ArrayList<>();
                List<ExecutionMode> executionModeSource = new ArrayList<>();
                List<ExecutionMode> executionModeStandard = new ArrayList<>();

                for (ExecutionMode executionMode : executionModes_fromJson) {

                    if (executionMode.getJID().equals("SINK") ) {
                            executionModeSink.add(executionMode);
                    }
                    else if(executionMode.getJID().equals("SOURCE") && executionMode.getId().equals(id)){
                        executionModeSource.add(executionMode);
                    }
                    else if(executionMode.getJID()!=null && executionMode.getId().equals(id)) {
                        executionModeStandard.add(executionMode);
                    }
                }
                for(Job job: jobs){
                    if(job.getJobType().equals(SINK) && job.getProject().getId().equals(i)){
                        job.setExecutionModes(executionModeSink);
                    }
                    else if(job.getJobType().equals(SOURCE) && job.getProject().getId().equals(i)){
                        job.setExecutionModes(executionModeSink);
                    }
                    else if( job.getProject().getId().equals(i)){
                        job.setJobType(STANDARD);
                        job.setExecutionModes(executionModeStandard);
                    }
                }
        }
    }

 private  List<Project> initProject() {
     List<Project> projects1 = new ArrayList<>();
     this.executionModes_fromJson = new ArrayList<>();
     int exMList_id =0;
     int project_id = 0;

     List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
     for (Map<String, Object> jsonProject : jsonProjects) {
         Project project = new Project();
         project.setId(String.valueOf(project_id++));
         String PID = (String) jsonProject.get("PID");
         project.setPID(PID);
         int priority = (int) jsonProject.get("Priority");
         project.setPriority(priority);
         int vb = (int) jsonProject.get("VB");

         project.setVb(vb);
         String gtin = (String) jsonProject.get("GTIN");
         project.setGtin(gtin);
         int np = (int) jsonProject.get("NP");
         project.setNp(np);

         projects1.add(project);

         List<Map<String, Object>> jsonExecutionModeList = (List<Map<String, Object>>) jsonProject.get("ExecutionModeList");
         for (Map<String, Object> jsonExecutionMode : jsonExecutionModeList) {
             ExecutionMode executionMode = new ExecutionMode();
             executionMode.setId(String.valueOf(project_id));
             executionMode.setJID((String) jsonExecutionMode.get("JID"));
             executionMode.setId(String.valueOf(project_id));
             executionMode.setDuration((int) jsonExecutionMode.get("Duration"));
             List<ResourceRequirement>resourceRequirementsList = new ArrayList<>();
             List<Map<String, Object>> jsonResourceRequirementList = (List<Map<String, Object>>) jsonExecutionMode.get("ResourceRequirementList");
             for (Map<String, Object> jsonResourceRequirement : jsonResourceRequirementList) {
                 ResourceRequirement resourceRequirement = new ResourceRequirement();
                 resourceRequirement.setId(String.valueOf(project_id));
                 resourceRequirement.setRID((String) jsonResourceRequirement.get("RID"));
                 resourceRequirement.setRequirement((int) jsonResourceRequirement.get("Requirement"));
                resourceRequirementsList.add(resourceRequirement);
             }
             executionMode.setResourceRequirements(resourceRequirementsList);
             this.executionModes_fromJson.add(executionMode);
         }
     }
     return projects1;
 }

 private List<Resource> initResource(){
        List<Resource> resources1 = new ArrayList<>();
        int id=0;
     List<Map<String, Object>> jsonResourceList = (List<Map<String, Object>>) jsonMap.get("ResourceList");
     for (Map<String, Object> jsonResource : jsonResourceList) {

         if (jsonResource.get("@type").equals("global")) {

             GlobalResource globalResource = new GlobalResource();
             globalResource.setId(String.valueOf(id++));
             String rid = (String) jsonResource.get("RID");
             globalResource.setRID(rid);
             int capacity = (int) jsonResource.get("Capacity");
             globalResource.setCapacity(capacity);

             List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
             if (RestrictionList == null) {
                 this.RestrictionList = restrictionList;
             } else {
                 this.RestrictionList = Collections.emptyList();
             }
             resources1.add(globalResource);

         } else if (jsonResource.get("@type").equals("local")) {
             LocalResource localResource = new LocalResource();
             localResource.setId(String.valueOf(id++));
             String rid = (String) jsonResource.get("RID");
             localResource.setRID(rid);
             int capacity = (int) jsonResource.get("Capacity");
             localResource.setCapacity(capacity);
             boolean renewable = (boolean) jsonResource.get("Renewable");
             localResource.setRenewable(renewable);
             List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
             if (RestrictionList == null) {
                 this.RestrictionList = restrictionList;
             } else {
                 this.RestrictionList = Collections.emptyList();
             }
             resources1.add(localResource);
         }

     }
     return resources1;
 }

    private List<Job> initJobList() {
        List<Map<String, Object>> jsonJobs = (List<Map<String, Object>>) jsonMap.get("JobList");
        int id =0;
        List<Job> jobs = new ArrayList<>();
        this.successorJobMap = new HashMap<>();
        for (Map<String, Object> jsonJob : jsonJobs) {
            Job job = new Job();
            job.setId(String.valueOf(id++));
            String jid = (String) jsonJob.get("JID");
            job.setJID(jid);
            if(job.getJID().equals("SOURCE") || job.getJID().equals("SINK")){
                job.setJobType(JobType.valueOf(job.getJID()));
            }
          else {
              job.setJobType(STANDARD);
            }
            List<String> successorList = (List<String>) jsonJob.get("SuccessorList");
            this.successorJobMap.put(job.getId(), successorList);
            jobs.add(job);
        }

        return jobs;
    }

    private void initBase() {
        this.ID = (String) jsonMap.get("ID");
        this.StartDate = (String) jsonMap.get("StartDate");
        this.EndDate = (String) jsonMap.get("EndDate");
        this.Termination = (String) jsonMap.get("Termination");
        this.ResourceList = new ArrayList<>();
        List<Map<String, Object>> jsonResourceList = (List<Map<String, Object>>) jsonMap.get("ResourceList");
        for (Map<String, Object> jsonResource : jsonResourceList) {

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



