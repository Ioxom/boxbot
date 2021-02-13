package io.ioxcorp.ioxbox.listeners;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;

public class HandleDelete implements Runnable {
    private final CustomUser user;

    public HandleDelete(CustomUser user) {
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println("started");
        WhatAmIDoing response = ConfirmationGetter.crab(user.id);
        if (response.getB()) {
            Main.boxes.remove(user.id);
            response.getChannel().sendMessage("deleted").queue();
        } else {
            response.getChannel().sendMessage("no").queue();
        }
    }
}