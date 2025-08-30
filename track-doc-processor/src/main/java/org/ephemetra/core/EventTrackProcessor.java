package org.ephemetra.core;

import com.alibaba.excel.EasyExcel;
import com.google.auto.service.AutoService;
import org.ephemetra.annotation.EventTrack;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.ephemetra.annotation.EventTrack")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EventTrackProcessor extends AbstractProcessor {

    private boolean generated = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (generated || annotations.isEmpty()) return false;
        List<EventInfo> events = new ArrayList<>();
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(EventTrack.class);
        for (Element element : elementsAnnotatedWith) {
            if (element.getKind() != ElementKind.METHOD) continue;
            EventTrack annotation = element.getAnnotation(EventTrack.class);
            String name = annotation.eventName();
            String desc = annotation.description().replace("${eventName}", name);
            events.add(new EventInfo(name, desc));
        }
        if (!events.isEmpty()) {
            generateExcel(events);
        }
        return true;
    }

    private void generateExcel(List<EventInfo> events) {
        try {
            FileObject resource = processingEnv.getFiler()
                    .createResource(StandardLocation.CLASS_OUTPUT, "", "EventTrack.xlsx");
            File file = new File(resource.toUri());
            EasyExcel.write(file, EventInfo.class).sheet("EventTrack").doWrite(events);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class EventInfo {
        private String eventName;
        private String description;

        public EventInfo() {}
        public EventInfo(String eventName, String description) {
            this.eventName = eventName;
            this.description = description;
        }

        public String getEventName() { return eventName; }
        public void setEventName(String eventName) { this.eventName = eventName; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }
}