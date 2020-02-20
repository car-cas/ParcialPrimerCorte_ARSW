package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MoneyLaunderingService {
    SuspectAccount updateAccountStatus(SuspectAccount suspectAccount);
    SuspectAccount getAccountStatus(SuspectAccount accountId) throws Exception;
    List<SuspectAccount> getSuspectAccounts();
}
