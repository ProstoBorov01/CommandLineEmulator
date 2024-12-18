package org.program;

import java.util.Date;

public class FileEntry {

    private final String name;
    private final Date modification_date;
    private final String permissions;

    public FileEntry(String name, Date modification_date, String permissions) {
        this.name = name;
        this.modification_date = modification_date;
        this.permissions = permissions;
    }

    public String getName() {
        return name;
    }

    public Date get_modification_date() {
        return modification_date;
    }

    public String getPermissions() {
        return permissions;
    }
}
