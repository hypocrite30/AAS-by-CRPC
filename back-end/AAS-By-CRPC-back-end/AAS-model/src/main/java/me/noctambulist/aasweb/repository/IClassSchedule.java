package me.noctambulist.aasweb.repository;

import me.noctambulist.aasweb.model.ClassSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author: Hypocrite30
 * @Date: 2023/5/1 23:08
 */
public interface IClassSchedule extends JpaRepository<ClassSchedule, Integer>, JpaSpecificationExecutor<ClassSchedule> {

    @Modifying
    @Query("UPDATE ClassSchedule c SET c.status='已退课' " +
            "WHERE c.studentId=?1 AND c.semesterId=?2 AND c.courseId=?3 AND c.courseNum=?4")
    int dropCourse(Long studentId, Integer semesterId, String courseId, String courseNum);

    List<ClassSchedule> findByTutorIdOrderByCourseIdAscCourseNumAsc(Long tutorId);

}