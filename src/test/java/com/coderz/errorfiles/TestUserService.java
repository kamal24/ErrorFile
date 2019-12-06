package com.coderz.errorfiles;


import com.coderz.errorfiles.DAO.UserRepo;
import com.coderz.errorfiles.Entity.User;
import com.coderz.errorfiles.Model.FileModel;
import com.coderz.errorfiles.Service.impl.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.when;

public class TestUserService {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepo userDAO;

    List<User> users = new ArrayList<>();

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    private void mockUser(){

        User user1 = new User(1L,"kamal@gmail.com","kamal");
        User user2 = new User(1L,"gaurav@gmail.com","gaurav");
        User user3 = new User(1L,"meena@gmail.com","meena");
        User user4 = new User(1L,"shaffy@gmail.com","shaffy");

        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);

        when(userDAO.findAll()).thenReturn(users);
    }

    @Test
    public void getAllEmails(){
        mockUser();

        List<String> existedEmails = userService.allExistedEmail();
        assertEquals(4,existedEmails.size());
        for (int i=0;i<existedEmails.size();i++) {
            assertEquals(users.get(i).getEmailaddress().toUpperCase(),existedEmails.get(i));
        }
    }

    @Test
    public void saveUser(){
        User user = new User(1L,"kamal@gmail.com","kamal");
        when(userDAO.save(any(User.class))).thenReturn(user);

        List<FileModel> correctList = new ArrayList<>();
        FileModel fileModel = new FileModel();
        fileModel.setErrors(null);
        fileModel.setName("kamal");
        fileModel.setEmail("kamal@gmail.com");
        fileModel.setRoles("SU#Admin");

        assertThat(userService.save(correctList),is(true));
    }

}
