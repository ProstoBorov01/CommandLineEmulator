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
        this.current_path = "";
    }

    public String get_current_path() { return this.current_path; }

    public String change_directory(String directory_name) {
        String new_path;

        if (directory_name.equals("..")) {
            if (this.current_path.isEmpty()) {
                System.out.println("Вы уже на самом верхнем уровне.");
                return this.current_path;
            }

            int last_separator = this.current_path.lastIndexOf('/');
            if (last_separator == -1) {
                new_path = "";
            } else {
                new_path = this.current_path.substring(0, last_separator);
            }

            System.out.println("Попытка сменить директорию на: " + (new_path.isEmpty() ? "/" : new_path));

            if (is_directory(new_path)) {
                this.current_path = new_path;
                System.out.println("Директория успешно изменена на: " + (this.current_path.isEmpty() ? "/" : this.current_path));
            } else {
                System.out.println("Ошибка: Невозможно перейти в родительскую директорию.");
            }
        } else {
            new_path = this.current_path.isEmpty() ? directory_name : this.current_path + "/" + directory_name;
            System.out.println("Попытка сменить директорию на: " + new_path);

            if (is_directory(new_path)) {
                this.current_path = new_path;
                System.out.println("Директория успешно изменена на: " + this.current_path);
            } else {
                System.out.println("Ошибка: Директория не найдена или не является директорией: " + directory_name);
            }
        }

        return this.current_path;
    }

    public ArrayList<FileEntry> list_files() throws ZipException {
        System.out.println(this.current_path);
        ArrayList<FileEntry> result = new ArrayList<FileEntry>();
        Enumeration<? extends ZipEntry> zipEntries = this.zip_file.entries();
        Set<String> directories = new HashSet<>();

        String normalizedCurrentPath = this.current_path;
        if (!normalizedCurrentPath.endsWith("/")) {
            normalizedCurrentPath += "/";
        }
        if (!normalizedCurrentPath.startsWith("/")) {
            normalizedCurrentPath = "/" + normalizedCurrentPath;
        }

        while (zipEntries.hasMoreElements()) {
            ZipEntry element = zipEntries.nextElement();
            String entryName = element.getName();

            if (!entryName.startsWith("/")) {
                entryName = "/" + entryName;
            }

            if (entryName.startsWith(normalizedCurrentPath)) {
                String relativePath = entryName.substring(normalizedCurrentPath.length());

                if (!relativePath.isEmpty() && !relativePath.contains("/")) {
                    Date modificationDate = new Date(element.getTime());
                    String permissions = this.get_permissions(new ZipArchiveEntry(element));
                    String fileName = relativePath;
                    FileEntry fileEntry = new FileEntry(fileName, modificationDate, permissions);
                    result.add(fileEntry);
                }

                else if (!relativePath.isEmpty() && relativePath.indexOf('/') == relativePath.length() - 1) {
                    String dirName = relativePath.substring(0, relativePath.length() - 1); // Убираем слеш
                    if (!directories.contains(dirName)) {
                        directories.add(dirName);
                        Date modificationDate = new Date(element.getTime());
                        String permissions = this.get_permissions(new ZipArchiveEntry(element));
                        FileEntry fileEntry = new FileEntry(dirName, modificationDate, permissions);
                        result.add(fileEntry);
                    }
                }
            }
        }

        return result;
    }


    public String read_file(String file_name) throws IOException {
        String fullPath = this.current_path.isEmpty() ? file_name : this.current_path + "/" + file_name;
        InputStream is = this.zip_file.getInputStream(this.zip_file.getEntry(fullPath));
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder string_builder = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            string_builder.append(line).append("\n");
        }

        is.close();
        return string_builder.toString();
    }

    public List<String> find(String path, String name) throws IOException {
        List<String> foundPaths = new ArrayList<>();
        Enumeration<? extends ZipEntry> entries = this.zip_file.entries();

        while (entries.hasMoreElements()) {
            ZipEntry element = entries.nextElement();
            String entryName = element.getName();

            if (entryName.contains(name)) {
                foundPaths.add(entryName);
                System.out.println("Объект найден" + entryName);
            }
        }

        return foundPaths;
    }


    public void close() throws IOException {
        this.zip_file.close();
    }

    private Boolean is_directory(String directory_path) {
        ZipEntry entry = this.zip_file.getEntry(directory_path);
        if (entry != null && entry.isDirectory()) {
            return true;
        }
        entry = this.zip_file.getEntry(directory_path + "/");
        return entry != null && entry.isDirectory();
    }

    private String get_permissions(ZipArchiveEntry entry) {
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
