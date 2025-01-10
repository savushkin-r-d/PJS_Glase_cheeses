package org.acme.projectjobschedule.data;
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
import org.acme.projectjobschedule.domain.Allocation;
import org.acme.projectjobschedule.domain.ProjectJobSchedule;

import static org.acme.projectjobschedule.domain.JobType.*;

public class DataModel extends JsonImporter {

    private String ID;
    private String StartDate;
    private String EndDate;
    private String Termination;

    private List<Resource> resources;
    private List <ExecutionMode> executionModes_fromJson;
    private Map<String, List<String>> successorJobMap;
    private List<ResourceRequirement> resourceRequirementList;
    private List<String> RestrictionList;

    public DataModel(String filepath) {
        super(filepath);
    }

    public List<Resource> getResourceList() {
        return resources;
    }

    public List<ExecutionMode> getExecutionModes_fromJson() {
        return executionModes_fromJson;
    }

    private List<ResourceRequirement> getResourceRequirements(){
        return resourceRequirementList;
    }

    public ProjectJobSchedule generateProjectJobSchedule(){

        ProjectJobSchedule projectJobSchedule = new ProjectJobSchedule();

        // Projects
        List<Project> projects = initProject();
        // Resources
        List<Resource> resources = initResource();
        // Jobs
        List<Job> jobsFromJson  = initJobList();
        List<Job> jobs = generateJobs(jobsFromJson, projects, resources);
        // Generate and add ExecutionModes
        generateExecutionMode(jobs, projects.size());
        List<ExecutionMode> executionModeList = getExecutionModes_fromJson();
        int i =0 ;
        for(ExecutionMode executionMode : executionModeList){
            executionMode.setId(String.valueOf(i++));
        }
        // ResourceRequirements
        List<ResourceRequirement> resourceRequirements = getResourceRequirements();
        // Allocations
        List<Allocation> allocations = generateAllocations(jobs);

        projectJobSchedule.setProjects(projects);
        projectJobSchedule.setResources(resources);
        projectJobSchedule.setJobs(jobs);
        projectJobSchedule.setExecutionModes(executionModeList);
        projectJobSchedule.setResourceRequirements(resourceRequirements);
        projectJobSchedule.setAllocations(allocations);
        for(Resource resource : resources) {
            for (ResourceRequirement requirement : resourceRequirements) {
                if (resource.getRID().equals(requirement.getRID())) {
                            requirement.setResource(resource);
                }
            }
        }

        return projectJobSchedule;
    }

    private  List<Project> initProject() {
        List<Project> projects1 = new ArrayList<>();
        this.executionModes_fromJson = new ArrayList<>();
        this.resourceRequirementList = new ArrayList<>();
        int project_id = 0;

        List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
        for (Map<String, Object> jsonProject : jsonProjects) {
            Project project = new Project();
            project.setId(String.valueOf(project_id));
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
                List<ResourceRequirement>resourceRequirementsList1 = new ArrayList<>();
                List<Map<String, Object>> jsonResourceRequirementList = (List<Map<String, Object>>) jsonExecutionMode.get("ResourceRequirementList");
                for (Map<String, Object> jsonResourceRequirement : jsonResourceRequirementList) {
                    ResourceRequirement resourceRequirement = new ResourceRequirement();
                    resourceRequirement.setId(String.valueOf(project_id));
                    resourceRequirement.setRID((String) jsonResourceRequirement.get("RID"));
                    resourceRequirement.setRequirement((int) jsonResourceRequirement.get("Requirement"));
                    resourceRequirementsList1.add(resourceRequirement);
                    resourceRequirementList.add(resourceRequirement);
                }
                executionMode.setResourceRequirements(resourceRequirementsList1);
                this.executionModes_fromJson.add(executionMode);
            }
            ++project_id;
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
                globalResource.setId(String.valueOf(id));
                globalResource.setRID((String) jsonResource.get("RID"));
                globalResource.setCapacity((int) jsonResource.get("Capacity"));

                List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
                if (RestrictionList == null) {
                    this.RestrictionList = restrictionList;
                } else {
                    this.RestrictionList = Collections.emptyList();
                }
                resources1.add(globalResource);

            } else if (jsonResource.get("@type").equals("local")) {
                LocalResource localResource = new LocalResource();
                localResource.setId(String.valueOf(id));
                localResource.setRID((String) jsonResource.get("RID"));
                localResource.setCapacity((int) jsonResource.get("Capacity"));
                localResource.setRenewable((boolean) jsonResource.get("Renewable"));
                List<String> restrictionList = (List<String>) jsonResource.get("RestrictionList");
                if (RestrictionList == null) {
                    this.RestrictionList = restrictionList;
                } else {
                    this.RestrictionList = Collections.emptyList();
                }
                resources1.add(localResource);
            }
            ++id;
        }
        return resources1;
    }

    private List<Job> initJobList() {
        List<Map<String, Object>> jsonJobs = (List<Map<String, Object>>) jsonMap.get("JobList");
        int id =0;
        List<Job> jobs = new ArrayList<>();
        successorJobMap = new HashMap<>();
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
            successorJobMap.put(job.getJID(), successorList);
            jobs.add(job);
        }
        return jobs;
    }

    private List<Job> generateJobs(List<Job> jobsFromJson, List<Project> projects, List<Resource> resources) {
        int jobsSize = jobsFromJson.size();
        List<Job> jobs = new ArrayList<>(jobsSize * projects.size());
        int jobsCountPerProject = jobsSize;
        int countJob = 0;
        for (Project project : projects) {
            // Generate the job list
            List<Job> jobsPerProject = new ArrayList<>(jobsCountPerProject);
            for (Job json_job : jobsFromJson) {
                jobsPerProject.add(new Job(String.valueOf(countJob++), project, json_job.getJobType(), json_job.getJID()));
            }
            // Add all jobs of the given project
            jobs.addAll(jobsPerProject);
        }
        for(Map.Entry<String, List<String>> entry : successorJobMap.entrySet()){
            String key = entry.getKey();
            List<String> strList = entry.getValue();
            for(Job job : jobs){
                List<Job> successorJobs = new ArrayList<>();
                if(job.getJID().equals(key)){
                    if(strList.isEmpty()){
                        successorJobs = Collections.emptyList();
                    }
                    else{
                        for(String str : strList){
                            for(Job job1 : jobs){
                                if(job1.equals(job)) continue;
                                if(job1.getJID().equals(str)) {
                                    successorJobs.add(job1);
                                };
                            }
                        }

                    }
                }
                job.setSuccessorJobs(successorJobs);
            }
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

                    if (executionMode.getJID().equals("SINK") && executionMode.getId().equals(id)) {
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
                    if(job.getJobType().equals(SINK) && job.getProject().getId().equals(id)){
                        job.setExecutionModes(executionModeSink);
                    }
                    else if(job.getJobType().equals(SOURCE) && job.getProject().getId().equals(id)){
                        job.setExecutionModes(executionModeSource);
                    }
                    else if( job.getProject().getId().equals(id)){
                        job.setJobType(STANDARD);
                        job.setExecutionModes(executionModeStandard);
                    }
                }
        }
    }

    private List<Allocation> generateAllocations(List<Job> jobs) {
        List<Allocation> allocations = new ArrayList<>(jobs.size());
        int doneDate = 0;
        for (int i = 0; i < jobs.size(); i++) {
            allocations.add(new Allocation(String.valueOf(i), jobs.get(i)));
        }
        // Set source, sink, predecessor and successor jobs
        for (int i = 0; i < jobs.size(); i++) {
            Allocation allocation = allocations.get(i);
            Allocation sourceAllocation = allocations.stream()
                    .filter(a -> a.getJob().getJobType() == SOURCE
                            && a.getJob().getProject().equals(allocation.getJob().getProject()))
                    .findFirst()
                    .get();
            Allocation sinkAllocation = allocations.stream()
                    .filter(a -> a.getJob().getJobType() == SINK
                            && a.getJob().getProject().equals(allocation.getJob().getProject()))
                    .findFirst()
                    .get();
            List<Allocation> predecessorAllocations = allocations.stream()
                    .filter(a -> !a.equals(allocation) && a.getJob().getSuccessorJobs().contains(allocation.getJob()))
                    .distinct()
                    .toList();
            List<Allocation> successorAllocations = allocation.getJob().getSuccessorJobs().stream()
                    .map(j -> allocations.stream().filter(a -> a.getJob().equals(j)).findFirst().get())
                    .toList();
            allocation.setSourceAllocation(sourceAllocation);
            allocation.setSinkAllocation(sinkAllocation);
            allocation.setPredecessorAllocations(predecessorAllocations);
            allocation.setSuccessorAllocations(successorAllocations);
            allocation.setPredecessorsDoneDate(doneDate);
            boolean isSource = allocation.getJob().getJobType() == SOURCE;
            boolean isSink = allocation.getJob().getJobType() == SINK;
            if (isSource || isSink) {
                allocation.setExecutionMode(allocation.getJob().getExecutionModes().get(0));
                allocation.setDelay(0);
                if (isSink) {
                    doneDate += 4;
                }
            }
        }

        return allocations;
    }

    private void initBase() {
        this.ID = (String) jsonMap.get("ID");
        this.StartDate = (String) jsonMap.get("StartDate");
        this.EndDate = (String) jsonMap.get("EndDate");
        this.Termination = (String) jsonMap.get("Termination");

    }
    public record Pair<K, V>(K key, V value) {}
}



