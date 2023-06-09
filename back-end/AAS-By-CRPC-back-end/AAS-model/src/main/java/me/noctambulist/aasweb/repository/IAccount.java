package me.noctambulist.aasweb.repository;

import me.noctambulist.aasweb.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * @Author: Hypocrite30
 * @Date: 2023/4/21 13:58
 */
@Repository
public interface IAccount extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {

    Optional<Account> findByUniqueId(Long id);

    @Modifying
    @Transactional
    void deleteByUniqueId(Long id);

}