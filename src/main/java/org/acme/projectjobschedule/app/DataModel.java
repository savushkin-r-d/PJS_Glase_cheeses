package org.acme.projectjobschedule.app;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.acme.projectjobschedule.domain.*;
import org.acme.projectjobschedule.domain.resource.Resource;
import org.acme.projectjobschedule.domain.ExecutionMode;

public class DataModel extends JsonImporter {

    private List<Project> projects;
    private List<Resource> resources;
    private List<ExecutionMode> executionModeList;
    private List<Job> jobs;
    private List<String> successorJobs;
    private List<ResourceRequirement> resourceRequirementList;
    private List<String> RestrictionList;

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
    System.out.println("ProjectList:");
    if (this.projects.isEmpty()) {
        System.out.println("ProjectList is empty");
    }
    else{
        for (Project project : this.projects) {
            System.out.println("PID:" + project.getId());
            System.out.println("Priority:" + project.getPriority());
            System.out.println("VB:" + project.getVb());
            System.out.println("GTIN:" + project.getGtin());
            System.out.println("NP:" + project.getNp());
            System.out.println();
        }
    }
}
    private void initProjectList(){
        List<Map<String, Object>> jsonProjects = (List<Map<String, Object>>) jsonMap.get("ProjectList");
        this.projects = new ArrayList<>();
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
            this.projects.add(project);
        }
        this.projects= Collections.emptyList();

    }
}

