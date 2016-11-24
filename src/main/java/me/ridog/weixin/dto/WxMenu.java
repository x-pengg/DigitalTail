package me.ridog.weixin.dto;

/**
 * Created by Tate on 2016/10/25.
 */
public class WxMenu {

    private String type;
    private String content;

    public WxMenu(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
