package implementation.config;

import fais.zti.oramus.gomoku.Mark;

public class BoardConfig {
    private static BoardConfig INSTANCE;


    private Mark firstMark;
    private int size = 10;
    private boolean periodic = false;
    private boolean debug = false;


    private BoardConfig() {
    }

    public static void initialize() {
        INSTANCE = new BoardConfig();
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

    public Mark getFirstMark() {
        return firstMark;
    }

    public void setFirstMark(Mark firstMark) {
        this.firstMark = firstMark;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug() {
        this.debug = true;
    }
}

