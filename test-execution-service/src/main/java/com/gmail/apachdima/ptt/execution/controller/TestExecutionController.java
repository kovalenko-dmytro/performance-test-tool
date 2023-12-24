package com.gmail.apachdima.ptt.execution.controller;

import com.gmail.apachdima.ptt.common.constant.common.CommonConstant;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionRequest;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionResponse;
import com.gmail.apachdima.ptt.common.dto.execution.TestExecutionStatusResponse;
import com.gmail.apachdima.ptt.execution.service.TestExecutionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

@Tag(name = "Test execution Service REST API")
@RestController
@RequestMapping(value = "/api/v1/test-execution")
@RequiredArgsConstructor
public class TestExecutionController {

    private final TestExecutionService testExecutionService;

    @PostMapping()
    public ResponseEntity<TestExecutionResponse> execute(
        @RequestBody @Valid TestExecutionRequest request,
        HttpServletRequest httpRequest,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        String executionId = UUID.randomUUID().toString();
        String currentUrl = httpRequest.getRequestURL().toString();
        URI uri = URI.create(currentUrl.concat(CommonConstant.SLASH.getValue() + executionId + "/progress"));
        testExecutionService.execute(executionId, request, currentUrl, locale);
        return ResponseEntity.accepted().location(uri).build();
    }

    @GetMapping(value = "/{executionId}/progress")
    public ResponseEntity<?> progress(@PathVariable String executionId) {
        TestExecutionStatusResponse status = testExecutionService.getLatestTestExecutionStatus(executionId);
        return Objects.isNull(status)
            ? ResponseEntity.noContent().build()
            : ResponseEntity.ok().body(status);
    }

    @GetMapping(value = "/{executionId}")
    public ResponseEntity<TestExecutionStatusResponse> getExecution(
        @PathVariable String executionId,
        @RequestParam(value = "locale", required = false, defaultValue = "en") Locale locale
    ) {
        return ResponseEntity.ok().body(testExecutionService.getExecution(executionId, locale));
    }
}
