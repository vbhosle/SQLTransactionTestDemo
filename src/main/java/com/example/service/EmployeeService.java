package com.example.service;

import com.example.model.Employee;
import com.example.repository.EmployeeRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

public class EmployeeService {

    private EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public int update(Long id, String name) {
        long version = getVersion(id);
        return employeeRepository.update(id, name, version+1);
    }

    public long getVersion(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
        return employee.getVersion();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public Employee get(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found"));
    }
}
