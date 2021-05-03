package com.ensias.prjappliation;

public class Item {
    User user;
    Tool tool;

    public Item(User user, Tool tool) {
        this.user = user;
        this.tool = tool;
    }

    public User getUser() {
        return user;
    }

    public Tool getTool() {
        return tool;
    }
}
