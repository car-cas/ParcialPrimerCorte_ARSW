package edu.eci.arsw.exams.moneylaunderingapi.model;

import org.springframework.stereotype.Component;

@Component
public class SuspectAccount {
    public String accountId;
    public int amountOfSmallTransactions;

    public SuspectAccount(){}

    public String getAccountId(){
        return accountId;
    }

    public int getAmountOfSmallTransactions(){
        return amountOfSmallTransactions;
    }

}
