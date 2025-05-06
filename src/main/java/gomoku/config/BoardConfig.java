package gomoku.config;

public class BoardConfig {
    private static final BoardConfig INSTANCE = new BoardConfig();

    // Board default parameters
    private int size = 15;
    private boolean periodic = false;

    // Strategy toggles
    private boolean winImmediatelyEnabled = true;
    private boolean blockOpponentEnabled = true;
    private boolean forkCreationEnabled = true;

    private BoardConfig() {
    }

    public static BoardConfig getInstance() {
        return INSTANCE;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isPeriodic() {
        return periodic;
    }

    public void setPeriodic(boolean periodic) {
        this.periodic = periodic;
    }

    public boolean isWinImmediatelyEnabled() {
        return winImmediatelyEnabled;
    }

    public void setWinImmediatelyEnabled(boolean enabled) {
        this.winImmediatelyEnabled = enabled;
    }

    public boolean isBlockOpponentEnabled() {
        return blockOpponentEnabled;
    }

    public void setBlockOpponentEnabled(boolean enabled) {
        this.blockOpponentEnabled = enabled;
    }

    public boolean isForkCreationEnabled() {
        return forkCreationEnabled;
    }

    public void setForkCreationEnabled(boolean enabled) {
        this.forkCreationEnabled = enabled;
    }
}

