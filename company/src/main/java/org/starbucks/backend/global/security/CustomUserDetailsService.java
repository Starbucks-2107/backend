package org.starbucks.backend.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.starbucks.backend.domain.employee.entity.Employee;
import org.starbucks.backend.domain.employee.repository.EmployeeRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String idNumber) {
        Optional<Employee> employee = employeeRepository.findByIdNumber(idNumber);

        return new CustomUserDetails(employee.get());
    }
}
