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
    //going to implement each command in a method

    // method to list the files in the current directory in a reverse order
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
   // important static variables
   static boolean keepgoing = true;// boolean to end the do while loop in main
   static Stack<String> stack = new Stack<String>();// a stack to store the commands

   // method to list the files in the current directory in a reverse order
   public static void lsR() {
        // Get the current directory
        File currentDirectory = new File(System.getProperty("user.dir"));

        // List the contents of the current directory
        File[] files = currentDirectory.listFiles();


        // Print the contents of the current directory in reverse order
        for (int i = files.length-1; i >= 0; i--) {
            System.out.println(files[i].getName());
        }
        
    }

    // a method to implement the echo command
  public void echo(String[] args) {
  if (args.length != 1) {
    System.out.println("Invalid arguments. Usage: echo <message>");
    return;
  }

  String message = args[0];
  System.out.println(message);
}
    // a method to print the current working directory
  public void pwd() {
  // storing the current directory in a string
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
            // Handling the entered paths with spaces wrapped
            String newDirPath = String.join(" ", args);

            // Removing the double quotations
            if (newDirPath.startsWith("\"") && newDirPath.endsWith("\"")) {
                newDirPath = newDirPath.substring(1, newDirPath.length() - 1);
            }

            // If the path is absolute
            if (newDirPath.startsWith("/") || newDirPath.matches("^[A-Za-z]:\\\\.*$")) {
                // Checking that the entered path is a directory path
                if (new File(newDirPath).isDirectory()) {
                    System.setProperty("user.dir", newDirPath);
                    System.out.println("The current working directory is: " + System.getProperty("user.dir"));

                } else {
                    System.out.println("Please make sure this is a valid directory path.");
                }

            } else {
                // Getting the target directory 
                String targetDirPath = currentDirPath + File.separator + newDirPath;

                //an if condition for Checking that the entered path is a directory path
                if (new File(targetDirPath).isDirectory()) {
                    System.setProperty("user.dir", targetDirPath);
                    System.out.println("The current working directory is: " + System.getProperty("user.dir"));

                } else {
                    System.out.println("Please make sure this is a valid directory path.");
                }
            }
        }
    }

    // a method to make new directories as much as we want in the current working directory
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
    // a method to make files in our directory nd other directories too
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
    // a method to delete files in current working directory
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
    //a method to remove empty directories
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
    // static method to cat one file as argument
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
    // static method to cat two files as arguments
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
    // a method to show the contents of a file
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
    // a method to get our CLI history using a stack
    public void history()
    {
        System.out.println(stack);
    }
    // a method to copy two files 
    public void cp(String[] s)
    {
        String source = s[0];
        String destination = s[1] ;

        try{
            File sourceFile = new File(source);
            File destinationFile = new File(destination);

            if (!sourceFile.exists()){
                System.out.println("the source file Not Found !");
                System.exit(1);
            }
            File parentDir = destinationFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            FileInputStream inputStream = new FileInputStream(sourceFile);
            FileOutputStream outputStream = new FileOutputStream(destinationFile);

            // Buffer to hold data while copying
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            // Close the streams
            inputStream.close();
            outputStream.close();

            System.out.println("File copied successfully.");

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error copying the file.");
        }
    }
    // a method for copying the contents of two directories
    public void cp_r(String[] s){
        String source = s[1];
        String destination = s[2] ;
        
        try{
            File sourceDirectory = new File(source);
            File destinationDirectory = new File(destination);

            if (!sourceDirectory.exists()) {
                System.out.println("Source directory does not exist.");
                System.exit(1);
            }

            if (!destinationDirectory.exists()) {
                destinationDirectory.mkdirs();
            }

            CFR(sourceDirectory, destinationDirectory);
            System.out.println("Directory copied successfully.");

        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error copying the directory.");
        }
    }
    // private method to recursively copy files
    private static void CFR(File source, File destination) throws IOException {
        if (source.isDirectory()) {
            if (!destination.exists()) {
                destination.mkdir();
            }

            String[] files = source.list();
            for (String file : files) {
                File srcFile = new File(source, file);
                File destFile = new File(destination, file);
                CFR(srcFile, destFile);
            }
        } else {
            try (InputStream in = new FileInputStream(source);
                 OutputStream out = new FileOutputStream(destination)) {

                byte[] buffer = new byte[1024];
                int length;
                while ((length = in.read(buffer)) > 0) {
                    out.write(buffer, 0, length);
                }
            }
        }
    }
    // a method for the word counter command
    public static void wc(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int lineCount = 0;
            int wordCount = 0;
            int charCount = 0;

            String line;
            //while loop to count the lines in our file
            while ((line = reader.readLine()) != null) {
                lineCount++;
                String[] words = line.trim().split("\\s+");// using regular expressions to count words
                wordCount += words.length;
                charCount += line.length();
            }

            System.out.println(lineCount + " " + wordCount + " " + charCount + " " + fileName);

        } catch (IOException e) {
            System.out.println("An error occurred while reading the file:");
            e.printStackTrace();
        }
    }

    

    //overriding this method to proberly parse the input
    @Override
    public boolean parse(String input) {
        String[] parts = input.trim().split("\\s+");
        this.commandName = parts[0];
        this.args = new String[parts.length - 1];
        System.arraycopy(parts, 1, this.args, 0, this.args.length);
        return true;
    }
    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){
        switch (getCommandName()) {// switch statement to choose which command to implement 
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
                //inner if condition to determine the appropriate method
                if (getArgs().length > 0) {
                    lsR();
                    break;
                }
                else
                {
                    ls();
                    break;
                }
            case "cat":
                cat(getArgs());
                break;
            case "history":
                history();
                break;    
            case "exit":
                keepgoing = false;
                break;
            case "cp":
                //inner if condition to determine the appropriate method
                if (getArgs().length > 2) {
                    cp_r(getArgs());
                    break;
                }
                else
                {
                    cp(getArgs());
                    break;
                }
            case "wc":
                wc(getArgs()[0]);
                break;
            default:
                System.out.println("wrong command!");
                break;
        }
    }
    // Our main program to run the CLI properly till the user chooses to exit
    public static void main(String[] args)
    {
        Terminal terminal = new Terminal();
        Scanner in = new Scanner(System.in);
        do {
            String input = in.nextLine();
            stack.push(input);// pushing our input to the stack for the history command
            terminal.parse(input);
            terminal.chooseCommandAction();
        } while (keepgoing);
    }
}
