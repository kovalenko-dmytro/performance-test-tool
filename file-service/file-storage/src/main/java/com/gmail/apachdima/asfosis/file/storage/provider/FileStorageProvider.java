package com.gmail.apachdima.asfosis.file.storage.provider;

import java.util.Locale;

public interface FileStorageProvider<FS> {

    FS provideService(Locale locale);
}
