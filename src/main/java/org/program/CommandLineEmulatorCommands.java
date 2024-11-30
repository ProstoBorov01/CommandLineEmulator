package org.program;

import java.io.IOException;
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
            return "Ошибка при закрытии файловой системы: " + e.getMessage();
        }
        return "Выход из эмулятора.";
    }

    public String ls_command() {
        StringBuilder result = new StringBuilder();

        try {
            List<FileEntry> files = fileSystem.listFiles();

            // Заголовок таблицы
            result.append(String.format("%-15s %-25s %-25s%n", "Permissions", "File Name", "Modification Time"));
            result.append("-----------------------------------------------------------------\n");

            // Форматирование даты
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            for (FileEntry file : files) {
                String permissions = file.getPermissions();
                String name = file.getName();
                String formattedDate = dateFormat.format(file.getModificationDate());

                // Форматируем строку вывода
                String line = String.format("%-15s %-25s %-25s%n", permissions, name, formattedDate);
                result.append(line);
            }
        } catch (IOException e) {
            return "Ошибка при выполнении команды ls: " + e.getMessage();
        }

        return result.toString();
    }

    public String mkdir_command(String directoryName) {
        // В виртуальной файловой системе создание директории в ZIP-архиве не поддерживается,
        // поскольку ZIP-файлы неизменяемы после создания. Поэтому возвращаем сообщение об ошибке.
        return "Ошибка: Создание директорий в виртуальной файловой системе не поддерживается.";
    }

    public String cd_command(String directoryName) {
        String message = fileSystem.changeDirectory(directoryName);
        return message;
    }

    public String pwd_command() {
        return fileSystem.getCurrentPath();
    }

    public String find_command(String searchString) {
        StringBuilder result = new StringBuilder();

        try {
            List<String> foundPaths = fileSystem.find(searchString);

            if (foundPaths.isEmpty()) {
                result.append("Совпадений не найдено.\n");
            } else {
                for (String path : foundPaths) {
                    result.append(path).append("\n");
                }
            }
        } catch (IOException e) {
            return "Ошибка при выполнении команды find: " + e.getMessage();
        }

        return result.toString();
    }

    public String cat_command(String fileName) {
        try {
            String content = fileSystem.readFile(fileName);
            return content;
        } catch (FileNotFoundException e) {
            return "Ошибка: Файл с именем " + fileName + " не найден.";
        } catch (IOException e) {
            return "Ошибка при чтении файла: " + e.getMessage();
        }
    }

    public String error_command() {
        return "Ошибка: Что-то пошло не так.";
    }

    // Реализация команды clear для режима CLI (если необходимо)
    /*
    public String clear_command() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException ex) {
            return "Ошибка: Что-то пошло не так.";
        }
        return "";
    }
    */
}
