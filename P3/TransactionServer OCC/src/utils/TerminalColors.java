/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package utils;

/**
 *
 * @author Usuario
 */
public interface TerminalColors {
    public static final String RESET_COLOR = "\033[0m";
    public static final String BOLD_COLOR = "\033[1m";
    public static final String RED_FOREGROUND_COLOR = "\033[31m";
    public static final String BRIGHT_RED_FOREGROUND_COLOR = "\033[91m";
    public static final String GREEN_BACKGROUND_COLOR = "\033[42m";
    public static final String BRIGHT_GREEN_BACKGROUND_COLOR = "\033[102m";
    public static final String OPEN_COLOR = "\033[30m\033[43m[\033[1m";
    public static final String COMMIT_COLOR = "\033[30m\033[42m[\033[1m";
    public static final String ABORT_COLOR = "\033[30m\033[41m[\033[1m";
    public static final String READ_COLOR = "\033[30m\033[47m[\033[1m";
    public static final String WRITE_COLOR = "\033[30m\033[47m[\033[1m";
    public static final String RESTARTED_COLOR = "\033[30m\033[47m[\033[1m";
}
