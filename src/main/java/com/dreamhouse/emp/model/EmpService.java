package com.dreamhouse.emp.model;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("empService")
public class EmpService {

    @Autowired
    private EmpRepository repository;

  //員工登入
    public EmpVO findByAccountAndPassword(String account, String password) {
        return repository.findByAccountAndPassword(account, password);
    }    

}