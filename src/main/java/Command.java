public class Command {
    private String command;
    private String param;

    public Command(String input) {
        System.out.println(input);
        int startParam = input.indexOf('(');
        int endParam = input.indexOf(')');
        if (startParam == -1 || endParam == -1) {
            this.command = "INVALID";
            this.param = "INVALID";
        } else {
            String command = input.substring(0, startParam);
            String param = input.substring(startParam + 1, endParam);
            this.command = command;
            this.param = param;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getParam() {
        return param;
    }
}
