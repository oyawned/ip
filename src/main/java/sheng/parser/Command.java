package sheng.parser;

/**
 * Represents all possible commands that the chatbot can execute.
 * AI-assisted: GitHub Copilot helped generate the initial enum structure
 * and suggested adding ARCHIVE as a new command type to match other verbs.
 */
public enum Command {
    BYE,
    LIST,
    MARK,
    UNMARK,
    DELETE,
    TODO,
    DEADLINE,
    EVENT,
    FIND,
    ARCHIVE  // AI-assisted: Copilot suggested this addition for the archive feature
}
