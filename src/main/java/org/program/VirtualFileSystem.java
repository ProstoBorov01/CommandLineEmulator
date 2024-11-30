package org.program;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import org.apache.commons.compress.archivers.zip.*;


public class VirtualFileSystem {

    private final ZipFile zip_file;
    private String current_path;

    public VirtualFileSystem(String zip_path) throws IOException {
        this.zip_file = new ZipFile(zip_path);
        this.current_path = "/";
    }

    public String get_current_path() { return this.current_path; }

    public String change_directory(String directory_name)  {
        if (is_directory(directory_name)) {
            current_path = this.current_path + "/" + directory_name;
        }

        return "";
    }

    public ArrayList<FileEntry> list_files() throws ZipException {
        ArrayList<FileEntry> result =  new ArrayList<FileEntry>();
        Enumeration<? extends ZipEntry> zip_entries = this.zip_file.entries();

        while (zip_entries.hasMoreElements()) {
            ZipEntry element = zip_entries.nextElement();

            Date modification_date = new Date(element.getTime());
            String permissions = this.getPermissions(new ZipArchiveEntry(element));
            String file_name = element.getName();
            FileEntry file_entry = new FileEntry(file_name, modification_date, permissions);
            result.add(file_entry);
        }

        return result;
    }

    public String read_file(String file_name) throws IOException {
        InputStream is = this.zip_file.getInputStream(this.zip_file.getEntry(file_name));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder string_builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            string_builder.append(line).append("\n");
        }

        is.close();
        return string_builder.toString();
    }

    public String find(String path, String name) throws IOException {
        Enumeration<? extends ZipEntry> entries = new ZipFile(path).entries();

        while (entries.hasMoreElements()) {
            ZipEntry element = entries.nextElement();
            ZipArchiveEntry entry = new ZipArchiveEntry(element);

            if (element.isDirectory()) {
                if (element.getName().equals(name)) {
                    return "Всё гуд";
                }

                find(this.current_path + "/" + element.getName(), name);
            }

            if (element.getName().equals(name)) {
                return "Всё гуд";
            }
        }

        return "Такого нет";
    }

    public void close() throws IOException {
        this.zip_file.close();
    }

    private Boolean is_directory(String directory_name) {
        String full_path = this.current_path + "/" + directory_name;
        ZipEntry entry = this.zip_file.getEntry(full_path);

        return entry != null && entry.isDirectory();
    }

    private String getPermissions(ZipArchiveEntry entry) {
        String defaultPermissions = entry.isDirectory() ? "drwxr-xr-x" : "-rw-r--r--";
        long externalAttributes = entry.getExternalAttributes();

        int unixMode = (int) ((externalAttributes >> 16) & 0xFFFF);
        if (unixMode == 0) {
            return defaultPermissions;
        }

        StringBuilder sb = new StringBuilder();

        if (entry.isDirectory()) {
            sb.append("d");
        } else {
            sb.append("-");
        }

        sb.append((unixMode & 0400) != 0 ? "r" : "-");
        sb.append((unixMode & 0200) != 0 ? "w" : "-");
        sb.append((unixMode & 0100) != 0 ? "x" : "-");

        sb.append((unixMode & 0040) != 0 ? "r" : "-");
        sb.append((unixMode & 0020) != 0 ? "w" : "-");
        sb.append((unixMode & 0010) != 0 ? "x" : "-");

        sb.append((unixMode & 0004) != 0 ? "r" : "-");
        sb.append((unixMode & 0002) != 0 ? "w" : "-");
        sb.append((unixMode & 0001) != 0 ? "x" : "-");

        return sb.toString();
    }
}
