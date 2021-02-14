package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;

public class HandleAdd implements Runnable {
    private final CustomUser user;
    private final CustomUser asker;

    public HandleAdd(CustomUser user, CustomUser asker) {
        this.user = user;
        this.asker = asker;
    }

    @Override
    public void run() {
        System.out.println("started");
        WhatAmIDoing response = ConfirmationGetter.crab(user.id);
        ConfirmationGetter.clean(user.id);
        if (response.getB()) {
            asker.getBox().add(user);
            response.getChannel().sendMessage("accepted, added to box").queue();
        } else {
            response.getChannel().sendMessage("refused").queue();
        }
    }
}