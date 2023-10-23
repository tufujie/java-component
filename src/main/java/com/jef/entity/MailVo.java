package com.jef.entity;

/**
 * @author Jef
 * @date 2020/4/4
 */
public class MailVo {
    private String to;

    private String title;

    private String content;

    // 消息id
    private String msgId;

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}