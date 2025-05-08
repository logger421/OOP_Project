package implementation.config;

import fais.zti.oramus.gomoku.Mark;

public class BoardConfig {
    private static final BoardConfig INSTANCE = new BoardConfig();
    public static final int MAX_SIZE = 15;
    public static final int MIN_SIZE = 10;

    private int size = 10;
    private boolean periodic = false;
    private Mark firstMark;


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

    public Mark getFirstMark() {
        return firstMark;
    }

    public void setFirstMark(Mark firstMark) {
        this.firstMark = firstMark;
    }
}

