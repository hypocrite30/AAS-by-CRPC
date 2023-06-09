package me.noctambulist.aasweb.service;

import me.noctambulist.aasweb.common.exception.CustomException;
import me.noctambulist.aasweb.common.result.ResultEnum;
import me.noctambulist.aasweb.repository.IAccount;
import me.noctambulist.aasweb.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @Author: Hypocrite30
 * @Date: 2023/4/21 14:00
 */
@Service
public class AccountService {

    private final IAccount iAccount;

    @Autowired
    public AccountService(IAccount iAccount) {
        this.iAccount = iAccount;
    }

    @Transactional
    public Account create(Account account) {
        return iAccount.saveAndFlush(account);
    }

    @Transactional
    public Account update(Long uniqueId, Account account) {
        Optional<Account> optionalAccount = iAccount.findByUniqueId(uniqueId);
        if (optionalAccount.isPresent()) {
            account.setId(optionalAccount.get().getId());
            return iAccount.saveAndFlush(account);
        } else {
            throw new CustomException(ResultEnum.ACCOUNT_NOT_EXISTS);
        }
    }

    public List<Account> findAll() {
        return iAccount.findAll();
    }

    public Account findByUniqueId(Long id) {
        return iAccount.findByUniqueId(id).orElse(null);
    }

    public void deleteByUniqueId(Long uniqueId) {
        iAccount.deleteByUniqueId(uniqueId);
    }

}
