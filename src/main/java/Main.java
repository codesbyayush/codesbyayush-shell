import java.util.Scanner;
import java.io.File;
import java.lang.ProcessBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.file.*;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;

        String currpath = System.getProperty("user.dir");


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
            } else if(input.contains("pwd")) {
                System.out.println(currpath);
            } else if(input.contains("cd")) {
                if(input.charAt(3) == '/'){
                    File file = new File(input.replace("cd ", ""));
                    if(file.exists()){
                        currpath = file.getAbsolutePath();
                    } else {
                        System.out.println("cd: " + input.replace("cd ", "") + ": No such file or directory");
                    }
                } else if(input.charAt(4) == '/' ) {
                    String absolutePath;
                   if(currpath.charAt(currpath.length() - 1) == '/')
                       absolutePath = currpath;
                    else {
                        absolutePath = currpath + "/";
                    }
                    absolutePath = absolutePath + input.replace("cd ./", "");
                    File file = new File(absolutePath);
                    if(file.exists()){
                        currpath = file.getAbsolutePath();
                    } else {
                        System.out.println("cd: " + input.replace("cd ", "") + ": No such file or directory");
                    }
                } else {
                        // for ../ and ./ cd support;
                        Path path = Paths.get(currpath);
                        int pathCount = path.getNameCount();
                        String changes = input.replace("cd ", "");
                        StringBuilder newPath = new StringBuilder(changes);
                        boolean fileavailable = true;
                        while(newPath.charAt(0) == '.'){
                            if(pathCount == 0) {
                                fileavailable = false;
                                break;
                            }
                            path = path.getParent();
                            newPath = newPath.delete(0,3);
                            pathCount--;
                        }
                        if(fileavailable){
                            File file = new File(path.toString() + "/" + newPath.toString());
                            if(file.exists()){
                                currpath = file.getAbsolutePath();
                            } else {
                                fileavailable = false;
                            }
                        }
                        if(!fileavailable){
                            System.out.println("cd: " + input.replace("cd ", "") + ": No such file or directory");

                        }
                    }
                } else
            if(invalidCommand(input, paths)){
                System.out.println(input + ": command not found");
            }
        }
    }

    static boolean invalidCommand(String input, String[] paths) throws IOException {
        boolean found = false;
        String command = input.split(" ")[0];
        for(String path : paths){
            File file = new File(path + "/" + command);
            if(file.exists()){
                Process pb = new ProcessBuilder(path + "/" + command, input.replace(command + " ", "")).start();
                BufferedReader output = new BufferedReader(new InputStreamReader(pb.getInputStream()));
                String line;
                while((line = output.readLine()) != null){
                    System.out.println(line);
                }
                output.close();
                found = true;
                break;
            }
        }
        return !found;
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
            case "pwd":
                System.out.println("pwd is a shell builtin");
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
