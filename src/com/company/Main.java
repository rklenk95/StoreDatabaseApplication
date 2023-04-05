package com.company;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scnr = new Scanner(System.in);
        boolean quit = false;
        String sql = null;
        ResultSet rs = null;
        int selection;
        Connection conn;


        while(!quit) {
            try {
                System.out.println("Login");
                String url = "jdbc:mysql://localhost:3307/gamestop";
                System.out.print("Enter user: ");
                String user = scnr.next();
                System.out.print("Enter password: ");
                String password =  scnr.next();
                System.out.println();

                conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement();

                do {
                    System.out.println("Main Menu");
                    System.out.print("Enter 1 to go to transactions menu\nEnter 2 to go to employee menu\nEnter 3 to go to game menu\n" +
                            "Enter 4 to go to store members menu\nEnter 5 to quit\nInput: ");
                    selection = scnr.nextInt();
                    System.out.println();

                    if (selection == 1) {
                        transactionMenu(scnr, stmt, rs, sql);
                    } else if (selection == 2) {
                        employeeMenu(scnr, stmt, rs, sql);
                    } else if (selection == 3) {
                        gameMenu(scnr, stmt, rs, sql);
                    } else if (selection == 4) {
                        memberMenu(scnr, stmt, rs, sql);
                    } else if (selection == 5) {
                        quit = true;
                    } else {
                        System.out.println("Please choose a valid input");
                        System.out.println();
                    }
                } while (!quit);
            } catch (Exception e) {
                System.out.println("Login Failed");
                System.out.println(e.getMessage());
                System.out.print("Enter -1 to end program or any other key to try again: ");
                String end = scnr.next();
                System.out.println();
                if(end.equals("-1"))
                    quit = true;

            }
        }
    }

    // TRANSACTION MENU
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void transactionMenu(Scanner scnr, Statement stmt, ResultSet rs, String sql) throws SQLException {
        int selection;

        System.out.println("Transaction Menu");
        System.out.print("Enter 1 to see transactions \nEnter 2 to add transactions\nEnter 3 to delete transaction\n" +
                "Enter any other number to return to main menu\nInput: ");
        selection = scnr.nextInt();
        System.out.println();

        //PRINTS ALL TRANSACTIONS
        if (selection == 1) {

            sql = "select Transactions_ID, Employee_ID, Game_ID, Date_Of, Member_ID from transactions;";

            rs = stmt.executeQuery(sql);

            while (rs.next()) {
                System.out.println("Transaction ID- " + rs.getString("Transactions_ID") +
                        "\t" + "Employee ID- " + rs.getString("Employee_ID") + "\t" + "Game ID- " + rs.getString("Game_ID") +
                        "\t" + "Transaction Date- " + rs.getString("Date_Of") + "\t" + "Member ID- " + rs.getString("Member_ID"));
            }


            sql = "SELECT Sum(Price) FROM games INNER JOIN transactions ON transactions.Game_ID = games.Game_ID";
            rs = stmt.executeQuery(sql);


            while (rs.next()) {
                System.out.println("Total Profits: $" + rs.getString("Sum(Price)"));
            }
            System.out.println();

        }

        //ADDS A TRANSACTION
        else if (selection == 2) {
            System.out.println("Using this format example: '4', '106', '2020/12/05', '3'");
            System.out.println("Input must have valid ID numbers");
            System.out.print("Enter employee ID, game ID, date of transaction, and member ID (enter member ID as 0 if not a member) : ");
            scnr.nextLine();
            String input = scnr.nextLine();
            System.out.println();

            sql = "INSERT INTO `gamestop`.`transactions` (`Employee_ID`, `Game_ID`, `Date_Of`, `Member_ID`) VALUES (" + input + ")";
            stmt.executeUpdate(sql);

        }
        //DELETES A TRANSACTION
        else if (selection == 3) {
            System.out.print("Enter a valid transaction ID to delete it: ");
            scnr.nextLine();
            String input = scnr.nextLine();

            sql = "DELETE FROM transactions WHERE Transactions_ID = " + input;
            stmt.executeUpdate(sql);
            System.out.println("Transaction " + input + " has been deleted");
            System.out.println();
        }
    }

    // EMPLOYEE MENU
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void employeeMenu(Scanner scnr, Statement stmt, ResultSet rs, String sql) throws SQLException {
        int selection;

        System.out.println("Employee Menu");
        System.out.print("Enter 1 to see employees \nEnter 2 to add employee\nEnter 3 to remove employee\n" +
                "Enter any other number to return to main menu\nInput: ");
        selection = scnr.nextInt();
        System.out.println();

        //PRINTS ALL EMPLOYEE
        if (selection == 1) {
            try {
                sql = "select Employee_ID, First_Name, Last_Name, Position, Hourly_Rate, Start_Date from employee";

                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    System.out.println("Employee ID- " + rs.getString("Employee_ID") + "\t" + "First Name- "
                            + rs.getString("First_Name") + "\t" + "Last Name- " + rs.getString("Last_Name")
                            + "\t" + "Position- " + rs.getString("Position") + "\t\t" + "Houryly Rate- $" + rs.getString("Hourly_Rate") + "\t\t" + "Start Date- " +
                            rs.getString("Start_Date"));
                    System.out.println();
                }

            } catch (SQLException e) {
                System.out.println("You do not have access to your selection");
            }
        }

        //ADDS A EMPLOYEE
        else if (selection == 2) {
            try {
                System.out.println("Using this format example: '7', 'Tom','Smith','manager', '12.75', '2020/12/05'");
                System.out.print("Enter employee ID, first name, last name,position, and start date: ");
                scnr.nextLine();
                String input = scnr.nextLine();
                System.out.println();

                sql = "INSERT INTO `gamestop`.`employee` (`Employee_ID`, `First_Name`, `Last_Name`, `Position`,`Hourly_Rate`, `Start_Date`) VALUES (" + input + ")";
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                System.out.println("You do not have access to your selection or your input was invalid");
                System.out.println();
            }

        }
        //DELETES A EMPLOYEE
        else if (selection == 3) {
            try {
                System.out.print("Enter a valid employee ID with no transactions to remove them: ");
                scnr.nextLine();
                String input = scnr.nextLine();

                sql = "DELETE FROM employee WHERE Employee_ID = " + input;
                stmt.executeUpdate(sql);
                System.out.println("Employee " + input + " has been removed");
                System.out.println();
            } catch (Exception e) {
                System.out.println("You do not have access to your selection or your input was invalid");
                System.out.println();
            }
        }
    }

    // GAME MENU
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void gameMenu(Scanner scnr, Statement stmt, ResultSet rs, String sql) throws SQLException {
        int selection;

        System.out.println("Game Menu");
        System.out.print("Enter 1 to see games \nEnter 2 to add game\nEnter 3 to delete game\n" +
                "Enter any other number to return to main menu\nInput: ");
        selection = scnr.nextInt();
        System.out.println();

        //PRINTS ALL GAMES
        if (selection == 1) {
            try {
                sql = "select Game_ID, Title,ESRB_Rating, Quantity, Price from games";

                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    System.out.println("Game ID- " + rs.getString("Game_ID") + "\t" + "Title- "
                            + rs.getString("Title") + "\t" + "ESRB Rating- " + rs.getString("ESRB_Rating")
                            + "\t" + "Quantity- " + rs.getString("Quantity") + "\t" + "Price- $" +
                            rs.getString("Price"));
                }
                System.out.println();

            } catch (Exception e) {
                System.out.println("You do not have access to your selection");
                System.out.println();
            }

        }

        //ADDS A GAME
        else if (selection == 2) {
            try {
                System.out.println("Using this format example: '242', 'Zelda','E','29', '40.0'");
                System.out.print("Enter Game ID, title, ESRB rating, quantity, and price: ");
                scnr.nextLine();
                String input = scnr.nextLine();
                System.out.println();

                sql = "INSERT INTO `gamestop`.`games` (`Game_ID`, `Title`, `ESRB_Rating`, `Quantity`, `Price`) VALUES (" + input + ")";
                stmt.executeUpdate(sql);
            } catch (Exception e) {
                System.out.println("You do not have access to your selection or your input was invalid");
                System.out.println();
            }

        }
        //DELETES A GAME
        else if (selection == 3) {
            try {
                System.out.print("Enter a valid game ID with no transactions to remove them: ");
                scnr.nextLine();
                String input = scnr.nextLine();

                sql = "DELETE FROM `gamestop`.`games` WHERE (`Game_ID` = " + input + ")";
                stmt.executeUpdate(sql);
                System.out.println("Game " + input + " has been removed");
                System.out.println();
            } catch (Exception e) {
                System.out.println("You do not have access to your selection or your input was invalid");
                System.out.println();
            }
        }
    }

    // MEMBER MENU
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public static void memberMenu(Scanner scnr, Statement stmt, ResultSet rs, String sql) throws SQLException {
        int selection;

            System.out.println("Member Menu");
            System.out.print("Enter 1 to see members \nEnter 2 to add member\nEnter 3 to remove member\n" +
                    "Enter any other number to return to main menu\nInput: ");
            selection = scnr.nextInt();
            System.out.println();

            //PRINTS ALL MEMBERS
            if (selection == 1) {
                try {
                    sql = "select Member_ID, First_Name, Last_Name, email from members";

                    rs = stmt.executeQuery(sql);

                    while (rs.next()) {
                        System.out.println("Member ID- " + rs.getString("Member_ID") + "\t" + "First Name- "
                                + rs.getString("First_Name") + "\t" + "Last Name- " + rs.getString("Last_Name")
                                + "\t" + "Email- " + rs.getString("Email"));
                    }
                    System.out.println();
                } catch (Exception e) {
                    System.out.println("You do not have access to your selection");
                    System.out.println();
                }

            }

            //ADDS A MEMBER
            else if (selection == 2) {
                try {
                    System.out.println("Using this format example: '7', 'Tom','Smith','tom@gmail.com', '32.0'");
                    System.out.print("Enter member ID, first name, last name,email, and store credit earned: ");
                    scnr.nextLine();
                    String input = scnr.nextLine();
                    System.out.println();

                    sql = "INSERT INTO `gamestop`.`members` (`Member_ID`, `First_Name`, `Last_Name`, `Email`, `Store_Credit_Earned`) VALUES (" + input + ")";
                    stmt.executeUpdate(sql);
                }
                catch(Exception e)
                {
                    System.out.println("You do not have access to your selection or your input was invalid");
                    System.out.println();
                }

            }
            //DELETES A MEMBER
            else if (selection == 3) {
                try {
                    System.out.print("Enter a valid member ID with no transactions to remove them: ");
                    scnr.nextLine();
                    String input = scnr.nextLine();

                    sql = "DELETE FROM member WHERE Member_ID = " + input;
                    stmt.executeUpdate(sql);
                    System.out.println("Member " + input + " has been removed");
                    System.out.println();
                }
                catch(Exception e)
                {
                    System.out.println("You do not have access to your selection or your input was invalid");
                    System.out.println();
                }
            }
        }
    }