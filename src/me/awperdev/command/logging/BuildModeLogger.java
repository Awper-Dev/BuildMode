package me.awperdev.command.logging;

import java.util.logging.Logger;

public class BuildModeLogger extends Logger {
    protected BuildModeLogger(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }
}
