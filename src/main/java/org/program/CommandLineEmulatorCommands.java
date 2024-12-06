package org.program;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class CommandLineEmulatorCommands implements Commands {

    protected VirtualFileSystem fileSystem;

    public CommandLineEmulatorCommands(String zipFilePath) throws IOException {
        this.fileSystem = new VirtualFileSystem(zipFilePath);
    }

    public String exit_command() {
        try {
            fileSystem.close();
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
        return "Exit from emulator.";
    }

    public String ls_command() {
        StringBuilder result = new StringBuilder();

        try {
            List<FileEntry> files = fileSystem.list_files();

            result.append(String.format("%-15s %-25s %-25s%n", "Permissions", "File Name", "Modification Time"));
            result.append("-----------------------------------------------------------------\n");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (FileEntry file : files) {
                String permissions = file.getPermissions();
                String name = file.getName();
                String formattedDate = dateFormat.format(file.get_modification_date());

                // Форматируем строку вывода
                String line = String.format("%-15s %-25s %-25s%n", permissions, name, formattedDate);
                result.append(line);
            }
        } catch (IOException e) {
            return "Error in proccess ls: " + e.getMessage();
        }

        return result.toString();
    }

    public String mkdir_command(String directoryName) {
        return "Error: not realized yet";
    }

    public String cd_command(String directoryName) {
        String message = fileSystem.change_directory(directoryName);
        if (message.isEmpty()) {
            return "New directory: " + fileSystem.get_current_path();
        } else {
            return message;
        }
    }

    public String pwd_command() {
        return fileSystem.get_current_path();
    }

    public String find_command(String searchString) {
        StringBuilder result = new StringBuilder();

        try {
            List<String> foundPaths = fileSystem.find(fileSystem.get_current_path(), searchString);

            if (foundPaths.isEmpty()) {
                result.append("No results");
            } else {
                for (String path : foundPaths) {
                    result.append(path).append("\n");
                }
            }
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }

        return result.toString();
    }


    public String cat_command(String fileName) {
        try {
            String content = fileSystem.read_file(fileName);
            return content;
        } catch (IOException e) {
            return "Ошибка при чтении файла: " + e.getMessage();
        }
    }

    public String error_command() {
        return "Error";
    }
}
