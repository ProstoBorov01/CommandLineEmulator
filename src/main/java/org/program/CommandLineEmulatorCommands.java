package org.program;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class CommandLineEmulatorCommands implements Commands {
    protected File current_directory;

    public CommandLineEmulatorCommands() {
        current_directory = new File(System.getProperty("user.dir"));
    }

    private String find_file_in_directory(File current_directory, String search_string) {
        File[] files_in_directory = current_directory.listFiles();
        assert files_in_directory != null;

        for (File file : files_in_directory) {

            System.out.println(file.getName());
            if (file.isDirectory()) find_file_in_directory(file, search_string);
            else if (file.getName().equals(search_string)) return "Файл с таким названием был успешно найден --> " + search_string;
        }

        return "Ошибка: Файл с таким названием не был найден --> " + search_string;
    }

    public String exit_command() {
        return this.current_directory.toString();
    }

    public String ls_command() {
        StringBuilder result = new StringBuilder();
        String[] files = this.current_directory.list();

        assert files != null;

        try {
            result.append(String.format("%-15s %-20s %-25s%n", "Permissions", "File Name", "Time"));

            for (String file_name : files) {

                File file = new File(this.current_directory, file_name);
                Path file_path = file.toPath();
                BasicFileAttributes attrs = Files.readAttributes(file_path, BasicFileAttributes.class);
                String permissions =
                        (file.canRead() ? "r" : "-") + (file.canWrite() ? "w" : "-") + (file.canExecute() ? "x" : "-");
                Date creationDate = new Date(attrs.creationTime().toMillis());
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formatted_date = time.format(creationDate);

                result.append(String.format("%-15s %-20s %-25s%n", permissions, file_name, formatted_date));
            }
        }
        catch (IOException ex) {
            return this.error_command();
        }

        return result.toString();
    }

    public String mkdir_command(String file_name) {
        File new_directory = new File(this.current_directory, file_name);

        if (new_directory.exists()) {
            return "Ошибка: Директория с таким именем уже существует --> " + file_name;
        }
        else {
            boolean flag = new_directory.mkdir();

            if (flag) return "Директория с названием" + " " + file_name + " " + "была успешно создана";
            else return "Ошибка: Что-то пошло не так при создании директории!";
        }
    }

    public String cd_command(String to) {
        File new_directory = new File(this.current_directory, to);

        if (new_directory.isDirectory()) {
            this.current_directory = new_directory;
            return "";
        }
        else return "Ошикба: Дируктория с таким названием не найдена " + to;
    }

    public String pwd_command() {
        return this.current_directory.toString();
    }

    public String find_command(String search_string) {
        return find_file_in_directory(this.current_directory, search_string);
    }

    public String cat_command(String file_name) {
        File read_file = new File(this.current_directory, file_name);

        try {
            if (read_file.isFile() && read_file.exists()) {
                Scanner scanner = new Scanner(read_file);

                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            } else return "Ошибка: Файл с таким называнием не был найден";
        }
        catch (FileNotFoundException exception) {
            return this.error_command();
        }

        return this.error_command();
    }

    public String error_command() {
        return "Ошибка, Что-то пошло не так";
    }
    // реализация команды clear для режима cli

//    public String clear_command() {
//        try {
//            if (System.getProperty("os.name").contains("Windows")) {
//                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
//            } else {
//                System.out.print("\033[H\033[2J");
//                System.out.flush();
//            }
//        }
//        catch (IOException | InterruptedException ex) {
//            System.out.println("Ошибка: Что-то пошло не так");
//        }
//    }
}
