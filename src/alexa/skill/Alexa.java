package alexa.skill;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

class Alexa {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<String> commands = new LinkedBlockingDeque();
        BlockingQueue<String> response = new LinkedBlockingDeque();
        String takeString;

//		while(command!=null) {
//			Scanner input2 = new Scanner(System.in);
//			System.out.print("Enter your input: ");
//			String command2 = input2.nextLine().toLowerCase();
//			commands.put(command2);
//		}
        Thread inputThread = new Thread() {
            public void run() {
                try {
                    do {
                        Scanner input = new Scanner(System.in);
                        System.out.print("Enter your input: ");
                        String command = input.nextLine().toLowerCase();
                        commands.put(command);
                        Thread.sleep(2000);
                    }while(response.take()!=null);

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
        String takeString = commands.take();
        if (takeString.toLowerCase().contains("alexa")) {
            System.out.println("Alexa is running...");
            System.out.println("----------------------------");
            skill s = new skill(takeString);
            String takestring;
            do {
                s.determine_state();
                if (s.state_comfirm()) {
                    s.state_process();
                } else {
                    System.out.println("----------------------------");
                    System.out.println("Alexa is closed...");
                }
                response.put("finished");
                takeString = commands.take();
                s.setcommand(takeString);
            } while (takeString != null);
        }

    }
}


