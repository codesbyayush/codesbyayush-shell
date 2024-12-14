import java.util.Scanner;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;

        String[] paths = System.getenv("PATH").split(File.pathSeparator);
        while(flag){
            System.out.print("$ ");
            String input = scanner.nextLine();

            if(input.contains("type")) {
                handleCommandType(input, paths);
            } else
            if (input.contains("exit")){
                flag = false;
            } else if(input.contains("echo")) {
                System.out.println(input.replace("echo ", ""));
            } else
            if(invalidCommand(input)){
                System.out.println(input + ": command not found");
            }
        }
    }

    static boolean invalidCommand(String input){
        return true;
    }

    static void handleCommandType(String input, String[] paths){
        String command = input.replace("type ", "");
        switch (command) {
            case "exit":
                System.out.println("exit is a shell builtin");
                break;
            case "echo":
                System.out.println("echo is a shell builtin");
                break;
            case "type":
                System.out.println("type is a shell builtin");
                break;
            default:
                boolean found = false;
                for(String path : paths){
                    File file = new File(path + "/" + command);
                    if(file.exists()){
                        System.out.println(command + " is " + file.getAbsolutePath());
                        found = true;
                        break;
                    }
                }
                if(!found)
                System.out.println( command + ": not found");
        }
    }
}
