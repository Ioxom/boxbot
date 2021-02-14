package io.ioxcorp.ioxbox.listeners.confirmation.handlers;

import io.ioxcorp.ioxbox.Main;
import io.ioxcorp.ioxbox.data.format.CustomUser;
import io.ioxcorp.ioxbox.data.format.WhatAmIDoing;
import io.ioxcorp.ioxbox.helpers.EmbedHelper;
import io.ioxcorp.ioxbox.listeners.confirmation.ConfirmationGetter;

public class HandleDelete implements Runnable {
    private final CustomUser user;

    public HandleDelete(CustomUser user) {
        this.user = user;
    }

    @Override
    public void run() {
        System.out.println("started");
        WhatAmIDoing response = ConfirmationGetter.crab(user.id);
        ConfirmationGetter.clean(user.id);
        EmbedHelper helper = new EmbedHelper(user);
        if (response.getB()) {
            Main.boxes.remove(user.id);
            response.getChannel().sendMessage(helper.successEmbed("successfully deleted your box!")).queue();
        } else {
            response.getChannel().sendMessage(helper.errorEmbed("received false response: did not delete box")).queue();
        }
    }
}