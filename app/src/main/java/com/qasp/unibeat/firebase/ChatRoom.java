package com.qasp.unibeat.firebase;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {

    private List<String> my_messages, other_messages;

    public ChatRoom(List<String> messages, String email) {
        setAllMessages(messages, email);
    }

    /*
    Used to send a message, then add and add to UI.
    Called after locally sending a message (and posting to firebase).
     */
    public void addMyMessage(String message) {
        my_messages.add(message);
        // TODO Update UI
    }

    /*
    Used to set all messages and update UI.
    Called after the firebase update of data
     */
    public void setAllMessages(List<String> messages, String email) {
        my_messages = new ArrayList<>();
        other_messages = new ArrayList<>();

        for (String message : messages) {
            if (message.startsWith(email)) {
                my_messages.add(getRest(message));
            } else {
                other_messages.add(getRest(message));
            }
        }
        // TODO Update UI
    }

    public List<String> getMyMessages() {
        return my_messages;
    }

    public List<String> getOtherMessages() {
        return other_messages;
    }

    /*
    Gets the rest of a string after the first colon.
    Used to clip the email in the beginning

    Example:
    - Input: email:message here:and a colon in the message
    - Returns: message here:and a colon in the message
     */
    private String getRest(String message) {
        String[] split = message.split(":");
        StringBuilder sb = new StringBuilder(split[1]);

        for (int i = 2; i < split.length; ++i) {
            sb.append(" " + split[i]);
        }

        return sb.toString();
    }
}
