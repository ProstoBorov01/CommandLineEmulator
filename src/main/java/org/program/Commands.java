package org.program;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Commands {
    public String exit_command();
    public String ls_command();
    public String cd_command(String to);
    public String pwd_command();
    public String find_command(String search_string);
    public String cat_command(String file_name);
//    public String clear_command();
    public String mkdir_command(String file_name);
}
