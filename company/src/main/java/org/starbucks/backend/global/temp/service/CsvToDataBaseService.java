package org.starbucks.backend.global.temp.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.starbucks.backend.domain.employee.entity.Department;
import org.starbucks.backend.domain.employee.entity.Team;
import org.starbucks.backend.domain.employee.repository.DepartmentRepository;
import org.starbucks.backend.domain.employee.repository.TeamRepository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvToDataBaseService {
    private final DepartmentRepository departmentRepository;
    private final TeamRepository teamRepository;

    @Transactional
    public void loadAndSaveDepartments() throws Exception {
        Resource resource = new ClassPathResource("csv/department/department.csv");
        InputStream inputStream = resource.getInputStream();
        Reader in = new InputStreamReader(inputStream);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        List<Department> departments = new ArrayList<>();

        for (CSVRecord record : records) {
            String departmentName = record.get(0); // 부서명

            Department department = Department.builder()
                    .department(departmentName)
                    .build();

            departments.add(department);
        }

        departmentRepository.saveAll(departments);
    }

    @Transactional
    public void loadAndSaveTeams() throws Exception {
        Resource resource = new ClassPathResource("csv/department/team.csv");
        InputStream inputStream = resource.getInputStream();
        Reader in = new InputStreamReader(inputStream);

        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(in);
        List<Team> teams = new ArrayList<>();

        for (CSVRecord record : records) {
            String departmentName = record.get(0);
            String teamName = record.get(1);

            Department department = departmentRepository.findByDepartment(departmentName)
                    .orElseThrow(() -> new RuntimeException("해당 부서가 존재하지 않습니다.: " + departmentName));

            Team team = Team.builder()
                    .team(teamName)
                    .department(department)
                    .build();

            teams.add(team);
        }

        teamRepository.saveAll(teams);
    }
}
