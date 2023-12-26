package com.gmail.apachdima.ptt.common.constant.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Error {

    ENTITY_NOT_FOUND("error.entity.not.found"),
    JWT_TOKEN_NOT_FOUND("error.token.not.found"),
    ENTITY_CREATION_EXCEPTION("error.entity.created"),
    VALIDATION_REQUEST("error.validation.request"),
    MISSING_REQUEST_PARAMETER("error.missing.request.parameter"),
    DATA_ACCESS("error.data.access"),
    NO_HANDLER_FOUND("error.no.handler.found"),
    HTTP_METHOD_NOT_ALLOWED("error.http.method.not.allowed"),
    MEDIA_TYPE_NOT_SUPPORTED("error.media.type.not.supported"),
    UNAUTHORISED("error.unauthorised"),
    INTERNAL_SERVER_ERROR_OCCURRED("error.internal.server.error.occurred"),
    FILE_STORAGE_VENDOR_UNSUPPORTED("error.file.storage.vendor.unsupported"),
    FILE_STORAGE_CREDENTIALS_NOT_FOUND("error.file.storage.credentials.not.found"),
    FILE_STORAGE_SERVICE_UNAVAILABLE("error.file.storage.service.unavailable"),
    FILE_STORAGE_UNABLE_UPLOAD("error.file.storage.unable.upload"),
    FILE_STORAGE_UNABLE_READ("error.file.storage.unable.read"),
    EMAIL_SEND_ERROR("error.email.send"),
    EMAIL_TEMPLATE_NOT_FOUND("error.email.template.not.found"),
    TEST_EXECUTION_START_COMMAND_ERROR("error.test.execution.command.start"),
    TEST_EXECUTION_READ_PROCESS_ERROR("error.test.execution.process.read"),
    TEST_EXECUTION_PROCESS_INTERRUPTION_ERROR("error.test.execution.process.interrupted"),
    TEST_EXECUTION_PROCESS_LOG_CREATION_ERROR("error.test.execution.log.created"),
    TEST_EXECUTION_PROCESS_TERMINATION_ERROR("error.test.execution.process.terminated"),
    LOG_CONTROLLER_EXECUTE("error.log.controller.execute");

    private final String key;
}
