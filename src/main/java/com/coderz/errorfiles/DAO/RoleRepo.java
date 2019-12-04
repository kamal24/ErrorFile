package com.coderz.errorfiles.DAO;

import com.coderz.errorfiles.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    @Query("SELECT r FROM Role r WHERE LOWER(r.name) = LOWER(:name)")
    public List<Role> findByName(@Param("name") String name);
}

