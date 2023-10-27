//import java.util.Scanner;
//import static java.lang.Math.*;
//import static java.lang.String.*;
import java.io.File;

class Parser {
    String commandName;
    String[] args;
//This method will divide the input into commandName and args
//where "input" is the string command entered by the user
    public boolean parse(String input){return true;}
    public String getCommandName(){return commandName;}
    public String[] getArgs(){return args;}
}

public class Terminal extends Parser{
    Parser parser;
    //Implement each command in a method, for example:

    //public String pwd(){}
    public void cd(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid arguments. Usage: cd <directory>");
            return;
        }
        String directory = args[0];
        // Perform the necessary logic to change the directory
        // Here, we'll simply print the directory that would be changed to
        System.out.println("Changing directory to: " + directory);
    }
    public void mkdir(String[] name){
        // Create a new folder with the given name
        File folder = new File(name[0]);
        boolean created = folder.mkdirs();
        if (created)
        {
            System.out.println("folder created.");
        }
        else
        {
            System.out.println("Unable to create folder.");
        }
    }

    //This method will choose the suitable command method to be called
    @Override
    public boolean parse(String input) {
        String[] parts = input.trim().split("\\s+");
        this.commandName = parts[0];
        this.args = new String[parts.length - 1];
        System.arraycopy(parts, 1, this.args, 0, this.args.length);
        return true;
    }
    public void chooseCommandAction(){
        switch (getCommandName()) {
            case "mkdir":
                mkdir(getArgs());
                break;
        
            default:
                break;
        }
    }
    public static void main(String[] args)
    {
        Terminal terminal = new Terminal();
        String input = "mkdir hreba";
        terminal.parse(input);
        terminal.chooseCommandAction();
    }
}

