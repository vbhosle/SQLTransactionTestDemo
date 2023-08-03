package com.example.repository;

import com.example.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    @Modifying
    @Query(value = "UPDATE employees " +
            "SET name = :name, version = :newVersion " +
            "WHERE id = :id")
    int update(@Param("id") Long id, @Param("name") String name, @Param("newVersion") long newVersion);
}
