package utils;

public enum LevelLog {
    INFO("INFO"),
    WARN("WARNING"),
    ERROR("ERROR");

    private final String level;

    LevelLog(String level) {
        this.level = level;
    }

    public String getLevel() {
        return level;
    }
}
