package org.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

public class CommandLineEmulator extends CommandLineEmulatorCommands {
    private HashMap<String, Runnable> commands = new HashMap<>();

    private void fill_available_commands(String function_parameter) throws IOException {
        commands.put("ls", this::ls_command);
        commands.put("cd", () -> cd_command(function_parameter));
        commands.put("exit", this::exit_command);
        commands.put("pwd", this::pwd_command);
        commands.put("find", () -> find_command(function_parameter));
        commands.put("cat", () -> cat_command(function_parameter));
//        commands.put("clear", this::clear_command);
        commands.put("mkdir", () -> mkdir_command(function_parameter));
        commands.put("error", this::error_command);
    }

    public Runnable execute(String key_command) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line;

        try {
            System.out.print(this.current_directory + " " + "> ");
            line = reader.readLine();
            String[] input = line.split(" ");

            if (input.length >= 2) fill_available_commands(input[1]);
            else fill_available_commands("");

            if (this.commands.containsKey(input[0])) return commands.get(input[0]);
            else return commands.get("error");
        } catch (IOException ex) {

            return commands.get("error");
        }
    }

}
