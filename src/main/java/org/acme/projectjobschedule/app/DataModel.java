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

import static org.acme.projectjobschedule.domain.JobType.SOURCE;
import static org.acme.projectjobschedule.domain.JobType.STANDARD;

public class DataModel extends JsonImporter {

    private String ID;
    private String StartDate;
    private String EndDate;
    private String Termination;

    private List<Project> projects;
    private List<Resource> resources;
    private HashMap <String,ExecutionMode> executionModeMap;
    private List<Job> jobs;
    private Map<String, List<String>> successorJobMap;
    private List<List<ResourceRequirement>> resourceRequirementList;
    private List<String> RestrictionList;
    private List<Resource> ResourceList;
   private Map<String,ResourceRequirement> resourceRequirementMap;

    public DataModel(String filepath) {
        super(filepath);
    }


    public List<Resource> getResourceList() {
        return resources;
    }

    public void initModelObject(){
        initBase();
        initProject();
        initJobList();
    }

    public ProjectJobSchedule generateProjectJobSchedule(){
        ProjectJobSchedule projectJobSchedule = new ProjectJobSchedule();

        // Projects
        List<Project> projects = initProject();
        List<Resource> resources1 = initResource();
        List<Job> jobs1  = initJobList();
        projectJobSchedule.setProjects(projects);
        projectJobSchedule.setResources(resources1);
        projectJobSchedule.setJobs(jobs1);
        return projectJobSchedule;

    }

 private   List<Project> initProject() {
     List<Project> projects1 = new ArrayList<>();
     this.executionModeMap = new HashMap<>();
     this.resourceRequirementMap = new HashMap<>();
     int project_id = 0;
     int executionMode_id = 0;
     int resourceRequirement_id = 0;

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

         List<Map<String, Object>> jsonExecutionModeList = (List<Map<String, Object>>) jsonProject.get("ExecutionModeList");
         for (Map<String, Object> jsonExecutionMode : jsonExecutionModeList) {
             ExecutionMode executionMode = new ExecutionMode();
             executionMode.setId(String.valueOf(executionMode_id++));
             String jid = (String) jsonExecutionMode.get("JID");
             executionMode.setId(jid);
             int duration = (int) jsonExecutionMode.get("Duration");
             executionMode.setDuration(duration);

             List<Map<String, Object>> jsonResourceRequirementList = (List<Map<String, Object>>) jsonExecutionMode.get("ResourceRequirementList");
             for (Map<String, Object> jsonResourceRequirement : jsonResourceRequirementList) {
                 ResourceRequirement resourceRequirement = new ResourceRequirement();
                 resourceRequirement.setId(String.valueOf(resourceRequirement_id++));
                 resourceRequirement.setRID((String) jsonResourceRequirement.get("RID"));
                 resourceRequirement.setRequirement((int) jsonResourceRequirement.get("Requirement"));
                 resourceRequirement.setExecutionMode(executionMode);
                 resourceRequirementMap.put(resourceRequirement.getId(), resourceRequirement);
             }

             executionModeMap.put(executionMode.getId(), executionMode);
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



