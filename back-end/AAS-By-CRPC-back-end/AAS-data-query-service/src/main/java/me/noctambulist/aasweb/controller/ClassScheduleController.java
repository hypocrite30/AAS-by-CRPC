package me.noctambulist.aasweb.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import me.noctambulist.aasweb.common.result.R;
import me.noctambulist.aasweb.common.util.JsonUtils;
import me.noctambulist.aasweb.model.ClassSchedule;
import me.noctambulist.aasweb.model.vo.ClassScheduleVO;
import me.noctambulist.aasweb.service.ClassScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * @Author: Hypocrite30
 * @Date: 2023/5/1 23:14
 */

@RestController
@RequestMapping("/class_schedule")
@Slf4j
@CrossOrigin
public class ClassScheduleController {

    private final ClassScheduleService classScheduleService;

    @Autowired
    public ClassScheduleController(ClassScheduleService classScheduleService) {
        this.classScheduleService = classScheduleService;
    }

    // =========================================================================================

    @PostMapping("/find_all")
    @ResponseBody
    public R findAll() {
        List<ClassSchedule> classSchedules = classScheduleService.findAll();
        return R.success(JsonUtils.newObjectNode().set("class_schedules", JsonUtils.objectToJsonNode(classSchedules)));
    }

    // =========================================================================================

    @PostMapping("/find_by_studentId_semesterId")
    @ResponseBody
    public R findByStudentIdAndSemesterId(@RequestBody @Validated final FindByStudentIdAndSemesterIdParam param)
            throws IOException {
        List<ClassSchedule> classSchedules =
                classScheduleService.findByStudentIdAndSemesterId(param.studentId, param.semesterId);
        ObjectNode response = JsonUtils.newObjectNode();
        ArrayNode classSchduleNode = JsonUtils.newArrayNode();
        Queue<String[]> sectionIdList = new LinkedList<>(), sectionNumList = new LinkedList<>(), weekList = new LinkedList<>();
        Queue<String> nameQueue = new LinkedList<>();
        for (ClassSchedule schedule : classSchedules) {
            Map<String, Object> classInfoMap = JsonUtils.fromJsonToMap(schedule.getClassInfo());
            String[] sectionIds = ((String) classInfoMap.get("section_id")).split(";");
            String[] sectionNums = ((String) classInfoMap.get("section_num")).split(";");
            String[] weeks = ((String) classInfoMap.get("week")).split(";");
            String courseName = (String) classInfoMap.get("course_name");
            sectionIdList.offer(sectionIds);
            sectionNumList.offer(sectionNums);
            weekList.offer(weeks);
            nameQueue.offer(courseName);
            classSchduleNode.add(JsonUtils.objectToJsonNode(schedule));
        }
        ClassScheduleVO[] classScheduleVOS = ClassScheduleVO.getInstance(sectionIdList, sectionNumList, weekList, nameQueue);
        response.set("class_schedules", classSchduleNode);
        response.set("schedule_tables", JsonUtils.objectToJsonNode(classScheduleVOS));
        return R.success(response);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class FindByStudentIdAndSemesterIdParam {
        @NotNull(message = "学生 id 不能为空")
        @JsonProperty("student_id")
        Long studentId;
        @NotNull(message = "学期 id 不能为空")
        @JsonProperty("semester_id")
        Integer semesterId;
    }

    // =========================================================================================

    @PostMapping("/find_by_tutorId")
    @ResponseBody
    public R findByTutorId(@RequestBody @Validated final FindByTutorIdParam param) throws IOException {
        ArrayNode classSchedules = classScheduleService.findByTutorId(param.tutorId);
        return R.success(JsonUtils.newObjectNode().set("class_schedules", classSchedules));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class FindByTutorIdParam {
        @NotNull(message = "教师 id 不能为空")
        @JsonProperty("tutor_id")
        Long tutorId;
    }

    // =========================================================================================

    @PostMapping("/find_classmate")
    @ResponseBody
    public R findClassmate(@RequestBody @Validated final FindClassmateParam param) {
        return R.success(classScheduleService.findClassmate(param.courseId, param.courseNum, param.tutorId));
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class FindClassmateParam {
        @NotNull(message = "课程号不能为空")
        @JsonProperty("course_id")
        String courseId;
        @NotNull(message = "课序号不能为空")
        @JsonProperty("course_num")
        String courseNum;
        @NotNull(message = "教师 id 不能为空")
        @JsonProperty("tutor_id")
        Long tutorId;
    }

}
