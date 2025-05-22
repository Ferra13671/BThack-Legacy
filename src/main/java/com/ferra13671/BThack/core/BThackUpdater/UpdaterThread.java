package com.ferra13671.BThack.core.BThackUpdater;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Constants;
import com.ferra13671.BThack.managers.managers.Thread.BThackThread;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class UpdaterThread extends BThackThread {
    private final URL url;
    private final Path filePath;

    private long downloadedBytes = 0;
    private UpdateState updateState = UpdateState.STARTED;

    public UpdaterThread(URL url, Path filePath) {
        this.url = url;
        this.filePath = filePath;
    }

    @Override
    public void threadAction() {
        File bthackFile = BThackUpdater.findBThackFile();
        if (bthackFile == null) {
            updateState = UpdateState.FAILED;
            try {
                Files.delete(filePath);
            } catch (IOException ignored) {}
            return;
        }
        File file = filePath.toFile();
        FileOutputStream fileOutputStream = null;
        BufferedInputStream bufferedInputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            bufferedInputStream = new BufferedInputStream(url.openStream());

            byte[] data = new byte[1024];
            int downloadedB;
            while ((downloadedB = bufferedInputStream.read(data, 0, 1024)) != -1) {
                fileOutputStream.write(data, 0, downloadedB);
                downloadedBytes += downloadedB;
            }

            updateState = UpdateState.SUCCESSFUL;


            fileOutputStream.close();
        } catch (Exception e) {
            BThack.error(e.getMessage());
            failed(fileOutputStream, bufferedInputStream);
        }
    }

    public void failed(FileOutputStream fileOutputStream, BufferedInputStream bufferedInputStream) {
        updateState = UpdateState.FAILED;
        try {
            Files.delete(filePath);
        } catch (IOException ignored) {}
        try {
            if (fileOutputStream != null) fileOutputStream.close();
            if (bufferedInputStream != null) bufferedInputStream.close();
        } catch (IOException ignored) {}
    }

    public String getDownloadedMBytes() {
        return Constants.DECIMAL_FORMAT.format(downloadedBytes / 1024f / 1024f);
    }

    public UpdateState getUpdateState() {
        return updateState;
    }
}
