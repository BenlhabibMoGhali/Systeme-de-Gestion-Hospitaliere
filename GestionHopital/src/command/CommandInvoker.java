package command;

public class CommandInvoker {

    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        if (command == null) {
            throw new IllegalStateException("Aucune commande définie.");
        }
        command.execute();
    }
}
