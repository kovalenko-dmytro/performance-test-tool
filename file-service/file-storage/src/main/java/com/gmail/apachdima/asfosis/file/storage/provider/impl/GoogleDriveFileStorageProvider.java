package com.gmail.apachdima.asfosis.file.storage.provider.impl;

import com.gmail.apachdima.asfosis.common.constant.message.Error;
import com.gmail.apachdima.asfosis.common.exception.AFSApplicationException;
import com.gmail.apachdima.asfosis.file.storage.provider.FileStorageProvider;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GoogleDriveFileStorageProvider implements FileStorageProvider<Drive> {

    private static final String APPLICATION_NAME = "asfosis";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);
    private static final String CREDENTIALS_FILE_PATH = "/google-drive-credentials.json";
    private static final String ACCESS_TYPE = "offline";
    private static final int RECEIVER_PORT = 8081;


    private final MessageSource messageSource;

    @Override
    public Drive provideService(Locale locale) {
        final NetHttpTransport httpTransport;
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            return new Drive.Builder(httpTransport, JSON_FACTORY, getCredentials(httpTransport, locale))
                .setApplicationName(APPLICATION_NAME)
                .build();
        } catch (GeneralSecurityException | IOException e) {
            throw new AFSApplicationException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_SERVICE_UNAVAILABLE.getKey(), new Object[]{e.getMessage()}, locale));
        }
    }

    private Credential getCredentials(final NetHttpTransport httpTransport, Locale locale) throws IOException {
        InputStream in = GoogleDriveFileStorageProvider.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (Objects.isNull(in)) {
            throw new FileNotFoundException(
                messageSource.getMessage(
                    Error.FILE_STORAGE_CREDENTIALS_NOT_FOUND.getKey(), new Object[]{CREDENTIALS_FILE_PATH}, locale));
        }
        GoogleClientSecrets clientSecrets =
            GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
            .setAccessType(ACCESS_TYPE)
            .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(RECEIVER_PORT).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
