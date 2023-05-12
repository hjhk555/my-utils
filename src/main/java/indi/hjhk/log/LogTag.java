package indi.hjhk.log;

public class LogTag {
    final LogTagContent content;
    public final boolean showOnScreen;

    public LogTag(LogTagContent content, boolean showOnScreen) {
        this.content = content;
        this.showOnScreen = showOnScreen;
    }

    public String getContent(){
        return content.getContent();
    }
}
