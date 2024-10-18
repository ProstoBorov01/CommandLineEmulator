package org.program;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.Supplier;

public class CommandLineEmulator extends CommandLineEmulatorCommands {
    private HashMap<String, Supplier<String>> commands = new HashMap<>();

    private void fill_available_commands(String function_parameter) throws IOException {
        commands.put("ls", this::ls_command);
        commands.put("cd", () -> cd_command(function_parameter));
        commands.put("exit", this::exit_command);
        commands.put("pwd", this::pwd_command);
        commands.put("find", () -> find_command(function_parameter));
        commands.put("cat", () -> cat_command(function_parameter));
        commands.put("mkdir", () -> mkdir_command(function_parameter));
        commands.put("error", this::error_command);
    }

    public String execute(String key_command, String arg) {
        try {
            if (arg != "") fill_available_commands(arg);
            else fill_available_commands("");

            if (this.commands.containsKey(key_command)) {
                return commands.get(key_command).get();
            } else {
                return commands.get("error").get();
            }
        } catch (IOException ex) {
            return commands.get("error").get();
        }
    }
}