package me.noctambulist.aasweb.repository;

import me.noctambulist.aasweb.model.StudentInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: Hypocrite30
 * @Date: 2023/4/23 11:54
 */
@Repository
public interface IStudentInfo extends JpaRepository<StudentInfo, Integer>, JpaSpecificationExecutor<StudentInfo> {

    Optional<StudentInfo> findByUniqueId(Long uniqueId);

    @Modifying
    @Transactional
    void deleteByUniqueId(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE StudentInfo s SET s.coupon=s.coupon+?1 WHERE s.uniqueId=?2")
    int addCoupon(Double amount, Long uniqueId);

}
