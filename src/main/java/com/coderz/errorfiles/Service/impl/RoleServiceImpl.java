package com.coderz.errorfiles.Service.impl;

import com.coderz.errorfiles.DAO.RoleRepo;
import com.coderz.errorfiles.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepo roleRepo;

    @Override
    public List<String> allRole() {
        List<String> validRoles = new ArrayList<>();
        roleRepo.findAll().stream().forEach(role->{
            validRoles.add(role.getName().toUpperCase());
        });

        return  validRoles;
    }
}
