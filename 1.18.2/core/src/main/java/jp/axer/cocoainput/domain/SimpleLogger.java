package jp.axer.cocoainput.domain;

public interface SimpleLogger {
    public void log(String msg,Object...data);

    public void error(String msg,Object...data);

    public void debug(String msg,Object...data);
}
