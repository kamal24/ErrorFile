package com.coderz.errorfiles.Service.impl;

import com.coderz.errorfiles.DAO.RoleRepo;
import com.coderz.errorfiles.DAO.UserRepo;
import com.coderz.errorfiles.Entity.Role;
import com.coderz.errorfiles.Entity.User;
import com.coderz.errorfiles.Model.FileModel;
import com.coderz.errorfiles.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserRepo userRepo;

    @Override
    public Boolean save(List<FileModel> correctList) {
            correctList.stream().forEach(idx->{
            User user = new User(idx.getEmail(),idx.getName());

            String[] roles = idx.getRoles().split("#");

            for (String rol : roles) {
                Role role = roleRepo.findByName(rol).get(0);
                user.getRoles().add(role);
            }

            userRepo.save(user);
        });
        return true;
    }

    @Override
    public List<String> allExistedEmail() {
        List<String> emails = new ArrayList<>();

        userRepo.findAll().stream().forEach(idx->{
            emails.add(idx.getEmailaddress().toUpperCase());
        });

        return emails;
    }
}
