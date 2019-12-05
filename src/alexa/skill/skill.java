package alexa.skill;

import java.util.List;

import static java.util.Arrays.asList;

import java.util.ArrayList;

public class skill {

    private String stateString;
    private String commandString;
    private Shopping_list shopping_list;
    private List<String> greeting_synonym = asList("hello", "hi", "hey");
    private List<String> add_synonym = asList("add", "put", "place", "insert", "set");
    private List<String> remove_synonym = asList("remove", "extract", "erase", "delete", "throwout", "extract", "pullout");
    private List<String> close_synonym = asList("close", "end", "stop", "finish", "turnoff", "shutdown", "sleep","bye");
    private List<String> state_list = asList("Add", "Remove", "Unidentified", "Closed", "Running","Greeting");
    private List<String> command_word = new ArrayList<String>();

    skill(String command) {
        commandString = command.replace("the", "");
        stateString = "Running";
        String[] arr = commandString.split("\\s+");
        for (String ss : arr) {
            command_word.add(ss.toLowerCase());
        }
        commandString = commandString.replace(" ", "");
        if (commandString.toLowerCase().contains("shoppinglist".toLowerCase())) {
            shopping_list = new Shopping_list();
        }
    }

    public void setcommand(String command) {

        command_word.clear();
        stateString = "Running";
        commandString = command.replace("the", "");
        String[] arr = commandString.split("\\s+");
        for (String ss : arr) {
            command_word.add(ss.toLowerCase());
        }
        commandString = commandString.replace(" ", "");
        try{
        if (commandString.toLowerCase().contains("shoppinglist".toLowerCase())&&shopping_list.shoppinglist.isEmpty()) {
            shopping_list = new Shopping_list();
        }}
        catch (NullPointerException e){
            shopping_list = new Shopping_list();
        }
    }


    public String determine_state() {
        boolean determine_add = false;
        boolean determine_remove = false;
        boolean determine_closed = false;
        boolean determine_greeting = false;
        if (stateString.equals("Running")) {
            for (String string : close_synonym) {
                determine_closed = determine_closed || commandString.contains(string);
            }
            for (String string : add_synonym) {
                determine_add = determine_add || commandString.contains(string);
            }
            for (String string : remove_synonym) {
                determine_remove = determine_remove || commandString.contains(string);
            }
            for (String string : greeting_synonym) {
                determine_greeting = determine_greeting || commandString.contains(string);
            }
            //System.out.println(determine_remove);
            if (determine_add) {
                stateString = "Add";
            } else if (determine_remove) {
                stateString = "Remove";
            } else if (determine_closed) {
                stateString = "Closed";
            } else  if(determine_greeting){
                stateString="Greeting";
            }
            else {
                stateString = "Unidentified";
            }
        }


        return stateString;
    }

    public boolean state_comfirm() {
        boolean determine = false;
        if (!stateString.equals("Closed")) {
            try {
                for (String string : state_list) {
                    determine = determine || stateString.equals(string);
                }
                if (determine) {
                    return true;
                } else {
                    throw new Exception("No such state.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        return false;

    }

    public void state_process() {
        switch (stateString) {
            case "Add":
                add();
                break;
            case "Remove":
                remove();
                break;
            case "Greeting":
                greeting();
                break;
            case "Unidentified":
                unidentified();
                break;
        }
    }

    public void greeting() {
        System.out.println("Hello. I'm Alexa.");
    }

    public void add() {

        String item = command_word.get(command_word.indexOf("add") + 1);
        try{
            shopping_list.Add(item);
            System.out.println(item + " has been added to the shopping list.");
            shopping_list.print_list();
        } catch (NullPointerException e){
            System.out.println("Missing shopping list.");
            System.out.println("Addition fail, because shopping list is not initialized");
        }

    }

    public void remove() {
        String item = command_word.get(command_word.indexOf("remove") + 1);
        try{
            shopping_list.Remove(item);
            System.out.println(item + " has been removed from the shopping list.");
            shopping_list.print_list();
        } catch (NullPointerException e){
            System.out.println("Missing shopping list.");
            System.out.println("Removal fail, because shopping list is not initialized");
        }
    }

    public void unidentified(){
        if (commandString.equals("alexa")){
            System.out.println("Yes, I'm here");
            System.out.println("Alexa is waiting your command, please re-enter your command");
        }
        else {
        System.out.println("Alexa cannot understand what you said, please re-enter your command");}
    }
}