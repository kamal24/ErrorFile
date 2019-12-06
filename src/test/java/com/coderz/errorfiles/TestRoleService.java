package com.coderz.errorfiles;

import com.coderz.errorfiles.DAO.RoleRepo;
import com.coderz.errorfiles.Entity.Role;
import com.coderz.errorfiles.Service.RoleService;
import com.coderz.errorfiles.Service.impl.RoleServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

public class TestRoleService {
    @InjectMocks
    RoleServiceImpl roleService;

    @Mock
    RoleRepo roleDAO;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAllRoles() {
        List<Role> roles = new ArrayList<>();
        Role role1 = new Role(1,"Admin");
        Role role2 = new Role(1,"SU");
        Role role3 = new Role(1,"USER");

        roles.add(role1);
        roles.add(role2);
        roles.add(role3);

        when(roleDAO.findAll()).thenReturn(roles);

        List<String> actualRoles = roleService.allRole();

        assertEquals(3,actualRoles.size());
        assertEquals("ADMIN",actualRoles.get(0));
        assertEquals("SU",actualRoles.get(1));
        assertEquals("USER",actualRoles.get(2));

        verify(roleDAO,times(1)).findAll();
    }
}
