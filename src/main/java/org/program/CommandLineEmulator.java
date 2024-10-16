package org.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CommandLineEmulator extends CommandLineEmulatorCommands {
    private HashMap<String, Runnable> commands = new HashMap<>();

    public void fill_available_commands(String function_parameter) throws IOException {
        commands.put("ls", this::ls_command);
        commands.put("cd", () -> cd_command(function_parameter));
        commands.put("exit", this::exit_command);
        commands.put("pwd", this::pwd_command);
        commands.put("find", () -> find_command(function_parameter));
        commands.put("cat", () -> cat_command(function_parameter));
        commands.put("clear", this::clear_command);
        commands.put("mkdir", () -> mkdir_command(function_parameter));
    }

    public void start_session() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        while (true) {
            System.out.print("> ");
            line = reader.readLine();
            String[] input = line.split(" ");

            if (input.length >= 2) fill_available_commands(input[1]);
            else fill_available_commands("");

            if (this.commands.containsKey(input[0])) commands.get(input[0]).run();
            else System.out.println("Неизвестная команда: " + input[0]);
        }
    }
}
