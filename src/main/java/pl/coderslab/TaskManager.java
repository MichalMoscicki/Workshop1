package pl.coderslab;

import org.apache.commons.lang3.StringUtils;

import javax.swing.text.Utilities;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class TaskManager {
    public static void main(String[] args) {

        //ładujemy listę
        String[][] tasks = loadFile();
        Scanner scanner = new Scanner(System.in);
        String usersChoice = "";

        while (!usersChoice.equals("exit")) {
            displayMenu();
            usersChoice = scanner.nextLine();
            switch (usersChoice) {
                case "add":
                    tasks = add(tasks);
                    break;
                case "remove":
                    tasks = remove(tasks);
                    break;
                case "list":
                    list(tasks);
                    break;
                case "exit":
                    break;
                default:
                    System.out.println("Inproper choice");
            }
        }
        System.out.println(ConsoleColors.RED + "Bye bye!" + ConsoleColors.RESET);
        saveFile(tasks);

    }

    //remove działa. poproś kogoś w poniedizałek, żeby to przejrzał
    public static String[][] remove(String[][] list) {


        System.out.println("Please select number to remove");

        int choice = validateNumber(list);

        return removeFromTable(list, choice);
    }

    private static String[][] removeFromTable(String[][] table, int indexToRemove) {

        String[][] updatedList = new String[table.length - 1][2];
        int newIndex = 0;
        for (int i = 0; i < table.length; i++) {
            if (i != indexToRemove) {
                updatedList[newIndex][0] = newIndex + " : ";
                updatedList[newIndex][1] = table[i][1];
                newIndex++;
            }
        }
        return updatedList;
    }

    private static int validateNumber(String[][] list) {
        Scanner scanner = new Scanner(System.in);
        int numberOfTasks = list.length;
        String choice;
        boolean isValid;

        do {
            isValid = true;
            System.out.println("Choose number from 0 to " + (numberOfTasks - 1) + ".");
            choice = scanner.nextLine();
            if (!StringUtils.isNumeric(choice)) {
                isValid = false;
            } else if (Integer.parseInt(choice) < 0 || Integer.parseInt(choice) > numberOfTasks - 1) {
                isValid = false;
            }

        } while (!isValid);
        return Integer.parseInt(choice);
    }

    public static void displayMenu() {
        System.out.println(ConsoleColors.BLUE + "Please select an option:" + ConsoleColors.RESET);
        String[] menuOptions = {"add", "remove", "list", "exit"};
        for (String option : menuOptions) {
            System.out.println(option);
        }
    }

    public static String[][] loadFile() {
        Path file = Paths.get("tasks.csv");
        StringBuilder stringBuilder = new StringBuilder();
        int verseCounter = 0;

        try (Scanner scan = new Scanner(file)) {
            // Arrays copy of

            while (scan.hasNextLine()) {
                stringBuilder.append(scan.nextLine()).append("\n");
                verseCounter++;
            }

        } catch (IOException e) {
            System.err.println("Cannot find file");
        }

        //znowu - tu kopiujemy listy, ale czy to da się zrobić łatwiej?

        String[] rawList = stringBuilder.toString().split("\n");
        String[][] readyList = new String[verseCounter][2];

        for (int i = 0; i < verseCounter; i++) {
            readyList[i][0] = i + " : ";
            readyList[i][1] = rawList[i];
        }
        return readyList;
    }

    public static void list(String[][] list) {

        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(list[i][j]);
            }
            System.out.println();
        }


    }

    public static String[][] add(String[][] list) {

        //przyjmujemy wartości - zapytaj jeszcze raz, czy to jest nie ok. Bo raczej jednak ok.

        StringBuilder newTask = new StringBuilder();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Please add task description");
        newTask.append(scanner.nextLine()).append(", ");

        System.out.println("Please add task due date");
        newTask.append(scanner.nextLine()).append(", ");

        System.out.println("Is your task important: true/false");
        newTask.append(scanner.nextLine());


        //dopisujemy
        //zrób za pomocą arrays copy of!!!!
        int newListLenght = list.length + 1;

        String[][] updatedList = new String[newListLenght][2];
        for (int i = 0; i < newListLenght - 1; i++) {
            updatedList[i][0] = i + " : ";
            updatedList[i][1] = list[i][1];
        }
        updatedList[newListLenght - 1][0] = updatedList.length - 1 + " : ";
        updatedList[newListLenght - 1][1] = newTask.toString();

        //czy to nie działa, bo jest lista dwuwymiarowa?
//        String[][] updatedList = Arrays.copyOf(list, list.length + 1);
//        updatedList[list.length ][0] = list.length +  " : " ;
//        updatedList[list.length ][1] = newTask.toString() ;


        return updatedList;
    }

    public static void saveFile(String[][] list) {

        Path file = Paths.get("tasks.csv");

        try (FileWriter fileWriter = new FileWriter(file.toFile(), false)) {
            for (int i = 0; i < list.length; i++) {
                fileWriter.append(list[i][1]);
                fileWriter.append("\n");
            }
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }
    }

}

//poproś kogoś o przejrzenie walidacji inputów, add i load file - problem stringBuildera i Arrays copy of

// na koniec wróc do gita (jeszcze raz sklonuj repo i dodaj ręcznie src :*
//ogarnąć repo


