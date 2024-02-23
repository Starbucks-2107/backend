package org.starbucks.backend.global.temp.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.starbucks.backend.global.temp.service.CsvToDataBaseService;

@RestController()
@RequestMapping(value = "/api/v1/data")
@RequiredArgsConstructor
@Slf4j
public class CsvToDataBaseController {
    StringBuilder sb = new StringBuilder();
    private final CsvToDataBaseService departmentService;

    @PostMapping(value = "/all")
    public String loadAndSaveAllData() throws Exception {
        departmentService.loadAndSaveDepartments();
        sb.append("부서 데이터 저장 성공!").append("\n");
        log.info("======================================================");
        log.info("부서 데이터 저장 성공!");

        departmentService.loadAndSaveTeams();
        sb.append("팀 데이터 저장 성공!").append("\n");
        log.info("======================================================");
        log.info("팀 데이터 저장 성공!");
        log.info("======================================================");

        return sb.toString();
    }

    @PostMapping(value = "/department")
    public String loadAndSaveDepartments() throws Exception {
        departmentService.loadAndSaveDepartments();

        return "부서 데이터 저장 성공!";
    }

    @PostMapping(value = "/team")
    public String loadAndSaveTeams() throws Exception {
        departmentService.loadAndSaveTeams();

        return "팀 데이터 저장 성공!";
    }
}
