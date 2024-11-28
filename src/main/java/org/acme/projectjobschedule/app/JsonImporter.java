package org.acme.projectjobschedule.app;

import org.acme.projectjobschedule.domain.ProjectJobSchedule;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.File;

public class JsonImporter {
    public ProjectJobSchedule importFromJson(String filePath) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(filePath);

        // Десериализация JSON-файла в объект ProjectJobSchedule
        ProjectJobSchedule projectJobSchedule = objectMapper.readValue(jsonFile, ProjectJobSchedule.class);

        return projectJobSchedule;
    }
}
