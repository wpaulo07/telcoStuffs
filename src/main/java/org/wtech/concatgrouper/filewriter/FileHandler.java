package org.wtech.concatgrouper.filewriter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileHandler {

    public static List<String> readLines(File file) throws IOException {
        return FileUtils.readLines(file, "UTF-8");
    }

    public static BufferedWriter getWriter(File file) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
    }
}