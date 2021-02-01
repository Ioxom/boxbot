package ca.ioxom.boxbot;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter {
    private boolean isCheckingForResponse;
    private User checkedUser;
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        switch (event.getMessage().getContentRaw().toLowerCase()) {
            case "-box add" :
                event.getChannel().sendMessage("proof that im getting the command").queue();
                /*try {
                    String addToBox = event.getMessage().getContentRaw().toLowerCase();
                    FileWriter myWriter = new FileWriter("C:\\Users\\darby\\Documents\\GitHub\\boxbot\\src\\main\\java\\ca\\ioxom\\boxbot\\Files\\boxmoment.txt");
                    myWriter.write("\n" + Arrays.toString(addToBox.split("-box add")));
                    myWriter.close();
                    event.getChannel().sendMessage("Succesfully added item" + addToBox + "to box").queue();
                    System.out.println("i tried \n\n\n\n\n\n\n\n\nagain");



                } catch (IOException e) {
                    System.out.println("did something wrong i think\n\n\n\n\n\nnotice me senpai");
                    e.printStackTrace();
                }*/
                break;
            case "-box yes" :
                event.getChannel().sendMessage("Box is here :package: ").queue();
                break;
        }
       /* ObjectMapper mapper = new ObjectMapper().setDefaultPrettyPrinter(new DefaultPrettyPrinter());
        try {
            mapper.writeValue(new File("box_data.json"), new Box(new CustomUser(event.getAuthor()), event.getAuthor()));
        } catch (Exception e) {
            e.printStackTrace();
        } */
    }
}
