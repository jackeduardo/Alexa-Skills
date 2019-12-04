package alexa.skill;

import java.util.List;

import static java.util.Arrays.asList;

import java.util.ArrayList;

public class skill {

    private String stateString;
    private String commandString;
    private Shopping_list shopping_list;
    private List<String> add_synonym = asList("add","put", "place", "insert", "set");
    private List<String> remove_synonym = asList("remove","extract", "erase", "delete", "throw out", "extract", "pull out");
    private List<String> state_list = asList("Add", "Remove", "Unidentified", "Closed", "Running");
    private List<String> command_word=new ArrayList<String>();

    skill(String command) {
        commandString = command;
        stateString = "Running";
        String [] arr = commandString.split("\\s+");
        for(String ss : arr){
            command_word.add(ss.toLowerCase());
        }
        if(command.toLowerCase().contains("shopping list".toLowerCase())) {
            shopping_list=new Shopping_list();
        }
    }
    public void setcommand(String command) {
        command_word.clear();
        stateString = "Running";
        commandString=command;
        String [] arr = commandString.split("\\s+");
        for(String ss : arr){
            command_word.add(ss.toLowerCase());
        }
    }


    public String determine_state() {
        boolean determine_add=false;
        boolean determine_remove=false;
        if (stateString.equals("Running")) {
            for (String string : add_synonym) {
                determine_add=determine_add||commandString.contains(string);
            }
            for (String string : remove_synonym) {
                determine_remove=determine_remove||commandString.contains(string);
            }
            //System.out.println(determine_remove);
            if (determine_add) {
                stateString = "Add";
            }
            else if (determine_remove) {
                stateString = "Remove";
            } else {
                stateString = "Unidentified";
            }
        }


        return stateString;
    }

    public boolean state_comfirm() {
        boolean determine=false;
        if (!stateString.equals("Closed")) {
            try {
                for (String string : state_list) {
                    determine=determine||stateString.equals(string);
                }
                if(determine){
                    return true;
                }
                else {
                    throw new Exception("No such state.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;

    }

    public void state_process() {
        if(stateString.equals("Add")) {
            add();
        }
        else if(stateString.equals("Remove")) {
            remove();
        }
        else if(stateString.equals("Greeting")) {
            greeting();
        }
    }

    public void greeting() {//not finished.
        System.out.println("Hello. I'm Alexa.");
    }

    public void add() {
        String item=command_word.get(command_word.indexOf("add")+1);
        shopping_list.Add(item);
        System.out.println(item+" has been added.");
        shopping_list.print_list();
    }

    public void remove() {
        String item=command_word.get(command_word.indexOf("remove")+1);
        shopping_list.Remove(item);
        System.out.println(item+" has been removed.");
        shopping_list.print_list();
    }
    public String getstate() {
        return stateString;
    }

}

