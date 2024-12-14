import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        boolean flag = true;
        while(flag){
            System.out.print("$ ");
            String input = scanner.nextLine();

            if (input.contains("exit")){
                flag = false;
            } else if(input.contains("echo")) {
                System.out.println(input.replace("echo ", ""));
            }else
            if(invalidCommand(input)){
                System.out.println(input + ": command not found");
            }
        }
    }

    static boolean invalidCommand(String input){
        return true;
    }

}
