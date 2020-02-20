package edu.eci.arsw.exams.moneylaunderingapi;


import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MoneyLaunderingController {

    @Autowired
    MoneyLaunderingService moneyLaunderingService;

    @GetMapping("/fraud-bank-accounts")
    public ResponseEntity<?> getOffendingAccounts(){
        try{
            return new ResponseEntity<>(moneyLaunderingService.getSuspectAccounts(), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/fraud-bank-accounts")
    public ResponseEntity<?> postOffendingAccounts(@RequestBody SuspectAccount suspectAccount ){
        try{
            return new ResponseEntity<>(moneyLaunderingService.updateAccountStatus(suspectAccount), HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/fraud-bank-accounts/{accountId}")
    public ResponseEntity<?> getAccountStatusId(@RequestBody SuspectAccount accountId){
        try{
            return new ResponseEntity<>(moneyLaunderingService.getAccountStatus(accountId), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/fraud-bank-accounts/{accountId}")
    public ResponseEntity<?> putAccountStatusId(@RequestBody SuspectAccount accountId){
        try{
            return new ResponseEntity<>(moneyLaunderingService.updateAccountStatus(accountId), HttpStatus.ACCEPTED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
