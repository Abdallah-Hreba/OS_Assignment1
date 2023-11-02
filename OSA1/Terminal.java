import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;



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

    public void ls() {
    // Get the current directory
          File currentDirectory = new File(System.getProperty("user.dir"));

    // List the contents of the current directory
         File[] files = currentDirectory.listFiles();

    // Sort the files alphabetically
         Arrays.sort(files);

        for (File file : files) {
            System.out.println(file.getName());
        }
   }
   static boolean keepgoing = true;
   static Stack<String> stack = new Stack<String>();

   public static void lsR() {
        // Get the current directory
        File currentDirectory = new File(System.getProperty("user.dir"));

        // List the contents of the current directory
        File[] files = currentDirectory.listFiles();

        // Sort the files in reverse alphabetical order
        Arrays.sort(files, Comparator.comparing(File::getName).reversed());

        // Print the contents of the current directory
        for (File file : files) {
            System.out.println(file.getName());
        }
    }


  public void echo(String[] args) {
  if (args.length != 1) {
    System.out.println("Invalid arguments. Usage: echo <message>");
    return;
  }

  String message = args[0];
  System.out.println(message);
}

  public void pwd() {
  // Get the current working directory
  String currentDirectory = System.getProperty("user.dir");

  // Print the current directory
  System.out.println(currentDirectory);}


    public void cd(String[] args) {
        // Get the user's current directory
        String currentDirPath = System.getProperty("user.dir");

        // Create a File object for the current directory
        File currentDirFile = new File(currentDirPath);

        if (args.length == 0) {
            // Get the user's home directory
            String homeDirPath = System.getProperty("user.home");

            // Change the current working directory to the user's home directory
            System.setProperty("user.dir", homeDirPath);
            System.out.println("The current working directory is: " + System.getProperty("user.dir"));

        } else if (args[0].equals("..")) {
            // Get the parent directory
            File parentDirFile = currentDirFile.getParentFile();

            // Change the current working directory to the parent directory
            if (parentDirFile != null) {
                System.setProperty("user.dir", parentDirFile.getAbsolutePath());
                System.out.println("The current working directory is: " + parentDirFile.getAbsolutePath());

            } else {
                System.out.println("The current directory is already the root directory.");
            }

        } else {
            // Handle the entered paths with spaces wrapped in double quotes
            String newDirPath = String.join(" ", args);

            // Remove the double quotations that may enclose the entered paths
            if (newDirPath.startsWith("\"") && newDirPath.endsWith("\"")) {
                newDirPath = newDirPath.substring(1, newDirPath.length() - 1);
            }

            // If the path is absolute
            if (newDirPath.startsWith("/") || newDirPath.matches("^[A-Za-z]:\\\\.*$")) {
                // Check that the entered path is a directory path
                if (new File(newDirPath).isDirectory()) {
                    System.setProperty("user.dir", newDirPath);
                    System.out.println("The current working directory is: " + System.getProperty("user.dir"));

                } else {
                    System.out.println("Please make sure this is a valid directory path.");
                }

                // If the path is relative
            } else {
                // Get the target directory path by combining the current directory path with the new directory path
                String targetDirPath = currentDirPath + File.separator + newDirPath;

                // Check that the entered path is a directory path
                if (new File(targetDirPath).isDirectory()) {
                    System.setProperty("user.dir", targetDirPath);
                    System.out.println("The current working directory is: " + System.getProperty("user.dir"));

                } else {
                    System.out.println("Please make sure this is a valid directory path.");
                }
            }
        }
    }

    
public void mkdir(String[] args) {
        if (args.length >= 1) {
            String currentDirectory = System.getProperty("user.dir");
            boolean success = true;

            for (String arg : args) {
                // Resolve the path against the current working directory
                Path itemPath = Paths.get(currentDirectory, arg);
                File item = itemPath.toFile();

                // Check if the item already exists
                if (item.isDirectory() || item.isFile()) {
                    System.out.println(arg + " already exists at the specified location.");
                } else {
                    boolean created = item.mkdirs();

                    if (created) {
                        System.out.println("Created: " + item.getAbsolutePath());
                    } else {
                        System.out.println("Failed to create: " + item.getAbsolutePath());
                        success = false;
                    }
                }
            }

            if (success) {
                System.out.println("All items created successfully.");
            } else {
                System.out.println("Some items were not created.");
            }
        } else {
            System.out.println("Invalid usage. Please provide item name(s).");
        }
    }

    public void touch(String[] name)
    {
        try {
            File filename = new File(name[0]);
            boolean created = filename.createNewFile();
            if (created) {
                System.out.println("File created successfully.");
            } else {
                System.out.println("Failed to create file!");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file:");
            e.printStackTrace();
        }
    }

    public void rm(String[] fname)
    {
        File f = new File(fname[0]);
        boolean deleted = f.delete();
        if (deleted)
        {
            System.out.println("file deleted succesfully.");
        }
        else{System.out.println("deletion failed.");}
    }

    public boolean deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        if (!deleteDirectory(file)) {
                            return false;
                        }
                    } else {
                        if (!file.delete()) {
                            return false;
                        }
                    }
                }
            }
        }
        return directory.delete();
    }

    public void rmdir(String[] args) {
        File directory = new File(args[0]);
        
        
        if (directory.exists() && directory.isDirectory() || directory.equals("*") ) {
        	if(directory.equals("*")) {
        		File current_directory = new File(".");
        		directory = new File(current_directory.getAbsolutePath());
        	}
            if(deleteDirectory(directory)) {
            	System.out.println("Directory " + directory + " removed successfully.");
            }
            else {System.out.println("Failed to remove directory " + directory + ".");}
        }
        
        else {System.out.println("Wrong argument.");}
      
    }

    static void OneFile(String file1) {
        try (BufferedReader br = new BufferedReader(new FileReader(file1))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.err.println("something wrong with the input");
        }
    }

    static void TwoFiles(String file1, String file2) {
        try (BufferedReader br1 = new BufferedReader(new FileReader(file1));
                BufferedReader br2 = new BufferedReader(new FileReader(file2))) {
            String line1;
            String line2;
            while ((line1 = br1.readLine()) != null) {
                System.out.println(line1);
            }
            while ((line2 = br2.readLine()) != null) {
                System.out.println(line2);
            }
        } catch (IOException e) {
            System.err.println("something wrong with the input");
        }
    }

    public void cat(String[] args)
    {
        int size = args.length;
        if ( size == 2) {
            TwoFiles(args[0], args[1]);
        }
        else if (size == 1) {
            OneFile(args[0]);
        }
    }

    public void history()
    {
        System.out.println(stack);
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
            case "cd":
                cd(getArgs());
                break;
            case "touch":
                touch(getArgs());
                break;
            case "rm" :
                rm(getArgs());
                break;
            case "rmdir" :
                rmdir(getArgs());
                break;    
            case "echo":
                echo(getArgs());
                break;
           case "pwd":
                pwd();
               break;
           case "ls":
                ls();
               break;
            case "ls -r":
                lsR();
               break;
            case "cat":
                cat(getArgs());
                break;
            case "history":
                history();
                break;    
            case "exit":
                keepgoing = false;
                break;
            default:
                System.out.println("wrong command!");
                break;
        }
    }
    public static void main(String[] args)
    {
        // Terminal terminal = new Terminal();
        // // Scanner in = new Scanner(System.in);
        // // String input = in.nextLine();
        // // terminal.parse(input);
        // // terminal.chooseCommandAction();
        // Stack<String> stack = new Stack<String>();
        // for (int i = 0; i < 6; i++) {
        //     Scanner myObj = new Scanner(System.in);
        //     String input = myObj.nextLine();
        //     stack.push(input);
        //     terminal.parse(input);
        //     terminal.chooseCommandAction();
        // }

        // System.out.println(stack);
        // boolean keepgoing = true;
        Terminal terminal = new Terminal();
        Scanner myObj = new Scanner(System.in);
        do {
            String input = myObj.nextLine();
            stack.push(input);
            terminal.parse(input);
            terminal.chooseCommandAction();
        } while (keepgoing);
    }
}
