package org.ephemetra.core;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.google.auto.service.AutoService;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.ephemetra.annotation.EventField;
import org.ephemetra.annotation.EventTrack;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.File;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@AutoService(Processor.class)
@SupportedAnnotationTypes("org.ephemetra.annotation.EventTrack")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EventTrackProcessor extends AbstractProcessor {

    private boolean generated = false;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (generated || annotations.isEmpty()) return false;
        List<EventMetadata> metadataList = new ArrayList<>();
        Set<? extends Element> elementsAnnotatedWith = roundEnv.getElementsAnnotatedWith(EventTrack.class);
        // 处理类注解
        for (Element classElement : elementsAnnotatedWith) {
            EventTrack classAnnotation = classElement.getAnnotation(EventTrack.class);
            String className = CommonUtils.defaultIfBlank(classAnnotation.name(), classElement.getSimpleName().toString());
            String classDesc = classAnnotation.desc();
            String classTrigger = classAnnotation.trigger();
            // 处理字段注解
            for (VariableElement fieldElement : ElementFilter.fieldsIn(classElement.getEnclosedElements())) {
                EventField fieldAnnotation = fieldElement.getAnnotation(EventField.class);
                String fieldName = CommonUtils.defaultIfBlank(fieldAnnotation.name(), fieldElement.getSimpleName().toString());
                String fieldType = fieldElement.asType().toString();
                String fieldDesc = classAnnotation.desc();
                EventMetadata metadata = new EventMetadata();
                metadata.setClassName(className);
                metadata.setClassDesc(classDesc);
                metadata.setClassTrigger(classTrigger);
                metadata.setFieldName(fieldName);
                metadata.setFieldType(fieldType);
                metadata.setFieldDesc(fieldDesc);
                metadataList.add(metadata);
            }
        }
        if (!metadataList.isEmpty()) {
            generateExcel(metadataList);
        }
        return true;
    }

    public String getFilename() {
        return String.format("EventTrack_%s.xlsx", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
    }

    private void generateExcel(List<EventMetadata> metadataList) {
        WriteCellStyle headStyle = new WriteCellStyle();
        headStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 给表头加边框
        headStyle.setBorderTop(BorderStyle.THIN);
        headStyle.setBorderBottom(BorderStyle.THIN);
        headStyle.setBorderLeft(BorderStyle.THIN);
        headStyle.setBorderRight(BorderStyle.THIN);
        WriteCellStyle contentStyle = new WriteCellStyle();
        contentStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 给内容加边框
        contentStyle.setBorderTop(BorderStyle.THIN);
        contentStyle.setBorderBottom(BorderStyle.THIN);
        contentStyle.setBorderLeft(BorderStyle.THIN);
        contentStyle.setBorderRight(BorderStyle.THIN);
        // 创建策略
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);
        try {
            FileObject resource = processingEnv.getFiler()
                    .createResource(StandardLocation.CLASS_OUTPUT, "", getFilename());
            File file = new File(resource.toUri());
            EasyExcel.write(file, EventMetadata.class)
                    .registerWriteHandler(styleStrategy)
                    .registerWriteHandler(new FirstColumnMergeHandler())
                    .sheet("服务端")
                    .doWrite(() -> metadataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class EventMetadata {
        @ExcelProperty("事件名称")
        @ColumnWidth(40)
        private String className;
        @ExcelProperty("事件描述")
        @ColumnWidth(40)
        private String classDesc;
        @ExcelProperty("触发时机")
        @ColumnWidth(40)
        private String classTrigger;
        @ExcelProperty("字段名称")
        @ColumnWidth(30)
        private String fieldName;
        @ExcelProperty("字段类型")
        @ColumnWidth(30)
        private String fieldType;
        @ExcelProperty("字段说明")
        @ColumnWidth(30)
        private String fieldDesc;

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getClassDesc() {
            return classDesc;
        }

        public void setClassDesc(String classDesc) {
            this.classDesc = classDesc;
        }

        public String getClassTrigger() {
            return classTrigger;
        }

        public void setClassTrigger(String classTrigger) {
            this.classTrigger = classTrigger;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getFieldDesc() {
            return fieldDesc;
        }

        public void setFieldDesc(String fieldDesc) {
            this.fieldDesc = fieldDesc;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            EventMetadata metadata = (EventMetadata) o;
            return Objects.equals(className, metadata.className) && Objects.equals(classDesc, metadata.classDesc) && Objects.equals(classTrigger, metadata.classTrigger) && Objects.equals(fieldName, metadata.fieldName) && Objects.equals(fieldType, metadata.fieldType) && Objects.equals(fieldDesc, metadata.fieldDesc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(className, classDesc, classTrigger, fieldName, fieldType, fieldDesc);
        }
    }
}