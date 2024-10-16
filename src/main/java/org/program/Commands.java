package org.program;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Commands {
    public void exit_command();
    public void ls_command() throws IOException;
    public void cd_command(String to);
    public void pwd_command();
    public void find_command(String search_string);
    public void cat_command(String file_name) throws FileNotFoundException;
    public void clear_command();
    public void mkdir_command(String file_name);
}
