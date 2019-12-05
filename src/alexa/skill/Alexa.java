package alexa.skill;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class Alexa {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> commands = new LinkedBlockingDeque();
        BlockingQueue<String> response = new LinkedBlockingDeque();
        System.out.println("Instruction:");
        System.out.println("This Alexa allow the key word such as 'add', 'remove' and some of their synonyms, but the typo is not allow.");
        System.out.println("This Alexa cannot process the multiple command concurrently, you cannot enter such like\" Hi Alexa, add the apple into the shopping list\"");
        System.out.println("Because it only has one state when it receive the command.");
        System.out.println("===========================================================");
        Thread inputThread = new Thread() {
            public void run() {
                try {
                    do {
                        String commandstr;
                        do {
                            Scanner input = new Scanner(System.in);
                            System.out.print("Alexa is sleeping... Enter your input (Use 'Alexa' to awake Alexa): ");
                            String command = input.nextLine().toLowerCase();
                            commandstr = command;
                        } while (!commandstr.toLowerCase().contains("alexa"));
                        commands.put(commandstr);
                        Thread.sleep(2000);
                    } while (Boolean.parseBoolean(response.take()));

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        Thread alexaThread = new Thread() {
            public void run() {
                try {
                    Thread.sleep(1000);
                    Alexa_run(commands, response);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        };
        inputThread.start();
        alexaThread.start();

    }

    public static void Alexa_run(BlockingQueue<String> commands, BlockingQueue<String> response)
            throws InterruptedException {
        String takenString = commands.take();
        if (takenString.toLowerCase().contains("alexa")) {
            System.out.println("Alexa is running...");
            System.out.println("----------------------------");
            skill s = new skill(takenString);
            boolean if_running = true;
            do {
                System.out.println("Alexa received your command. ");
                s.determine_state();
                if (s.state_comfirm()) {
                    s.state_process();
                } else {
                    if_running = false;
                    System.out.println("----------------------------");
                    System.out.println("ByeBye");
                    System.out.println("Alexa is closed...");
                }
                response.put(String.valueOf(if_running));
                if (if_running) {
                    takenString = commands.take();
                }
                s.setcommand(takenString);
            } while (takenString != null && if_running);
        }

    }
}

