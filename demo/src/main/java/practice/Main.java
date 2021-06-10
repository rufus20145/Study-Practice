package practice;

import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Main {
    private static final int TIME_TO_SLEEP = 500;
    private static final int THIRD_ACTION = 3;
    private static final int SECOND_ACTION = 2;
    private static final int FIRST_ACTION = 1;
    private static final int MIN_NUMBER_OF_ACTION = 1;
    private static final int MAX_NUMBER_OF_ACTION = 3;
    private static final String MAIN_MENU_STRING = "Выберите номер действия, которое вы хотите выполнить:\n1)Проиграть звук из файла\n2)Записать звук с микрофона в файл\n3)Записать звук с микрофона и проиграть его.";

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println(MAIN_MENU_STRING);
        int actionNumber;
        do {
            actionNumber = getIntValue();
            if (actionNumber < MIN_NUMBER_OF_ACTION || actionNumber > MAX_NUMBER_OF_ACTION) {
                System.out.println("Действия с введенным номером не существует. Повторите попытку");
            }
        } while (actionNumber < MIN_NUMBER_OF_ACTION || actionNumber > MAX_NUMBER_OF_ACTION);
        switch (actionNumber) {
            case FIRST_ACTION:
                FirstCase.start();
                break;
            case SECOND_ACTION:
                SecondCase.start();
                break;
            case THIRD_ACTION:
                ThirdCase.start();
                break;
            default:
                System.out.println("Вы попали туда, куда попасть было нельзя. Поздравляю!");
                break;
        }
        try {
            Thread.sleep(TIME_TO_SLEEP);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
        System.out.println("Действие завершено. Выход из программы.");
    }

    protected static int getIntValue() {
        int value = 0;
        boolean exceptionCaught = false;

        do {
            exceptionCaught = false;
            try {
                value = input.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Введенное значение не является целым числом. Повторите попытку.");
                exceptionCaught = true;
                input.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Вы не ввели ничего. Повторите попытку.");
                exceptionCaught = true;
                input.nextLine();
            } catch (IllegalStateException e) {
                System.out.println("Система ввода оказалась в некорректном состоянии. Повторите попытку.");
                exceptionCaught = true;
                input = new Scanner(System.in);
                input.nextLine();
            }
        } while (exceptionCaught);
        getStringValue();
        return value;
    }

    protected static String getStringValue() {
        return getStringValue(null);
    }

    protected static String getStringValue(String stringFormatRegex) {
        boolean exceptionCaught = false;
        String string = null;
        do {
            try {
                string = input.nextLine();
                if (stringFormatRegex != null && !string.matches(stringFormatRegex)) {
                    System.out
                            .println("Строка должна соответствовать формату (1,2,3) или (1, 2, 3). Повторите попытку");
                    exceptionCaught = true;
                } else {
                    exceptionCaught = false;
                }
            } catch (NoSuchElementException e) {
                System.out.println("Вы не ввели ничего. Повторите попытку.");
                exceptionCaught = true;
                input.nextLine();
            } catch (IllegalStateException e) {
                System.out.println("Система ввода оказалась в некорректном состоянии. Повторите попытку.");
                exceptionCaught = true;
                input = new Scanner(System.in);
            }
        } while (exceptionCaught);
        return string;
    }
}
