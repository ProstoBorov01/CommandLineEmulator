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

    private File current_directory;

    public CommandLineEmulatorCommands() {
        current_directory = new File(System.getProperty("user.dir"));
    }

    private String find_file_in_directory(File current_directory, String search_string) {
        File[] files_in_directory = current_directory.listFiles();
        assert files_in_directory != null;

        for (File file : files_in_directory) {

            if (file.isDirectory()) find_file_in_directory(file, search_string);
            else if (file.getName().equals(search_string)) return "Файл с таким названием был найден " + search_string;
        }

        return "Ошибка: Файл с таким названием не был найден " + search_string;
    }

    public void exit_command() {
        System.out.println(this.current_directory + "Exiting...");
        System.exit(0);
    }

    public void ls_command() {
        String[] files = this.current_directory.list();

        assert files != null;

        try {
            for (String file_name : files) {
                File file = new File(this.current_directory, file_name);
                Path file_path = file.toPath();
                BasicFileAttributes attrs = Files.readAttributes(file_path, BasicFileAttributes.class);
                String permissions =
                        (file.canRead() ? "r" : "-") + (file.canWrite() ? "w" : "-") + (file.canExecute() ? "x" : "-");
                Date creationDate = new Date(attrs.creationTime().toMillis());
                SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                time.format(creationDate);

                System.out.println(permissions + file_name + time);
            }
        }
        catch (IOException ex) {
            System.out.println("Ошибка: Что-то пошло не так");
        }
    }

    public void mkdir_command(String file_name) {
        File new_directory = new File(this.current_directory, file_name);

        if (new_directory.exists()) {
            System.out.println("Ошибка: Директория с таким именем уже существует: " + file_name);
        }
        else {
            boolean flag = new_directory.mkdir();

            if (flag) System.out.println("Директория с названием" + " " + file_name + " " + "была успешно создана");
            else System.out.println("Ошибка: Что-то пошло не так при создании директории!");
        }
    }

    public void cd_command(String to) {
        File new_directory = new File(this.current_directory, to);

        if (new_directory.isDirectory()) this.current_directory = new_directory;
        else System.out.println("Ошикба: Дируктория с таким названием не найдена " + to);
    }

    public void pwd_command() {
        System.out.println(this.current_directory);
    }

    public void find_command(String search_string) {
        System.out.println(find_file_in_directory(this.current_directory, search_string));
    }

    public void cat_command(String file_name) {
        File read_file = new File(this.current_directory, file_name);

        try {
            if (read_file.isFile() && read_file.exists()) {
                Scanner scanner = new Scanner(read_file);

                while (scanner.hasNextLine()) {
                    System.out.println(scanner.nextLine());
                }
            } else System.out.println("Ошибка: Файл с таким называнием не был найден");
        }
        catch (FileNotFoundException exception) {
            System.out.println("Ошибка: Что-то пошло не так");
        }
    }

    public void clear_command() {

    }
}
