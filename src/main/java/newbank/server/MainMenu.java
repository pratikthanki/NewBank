package newbank.server;

import java.io.PrintWriter;

    public enum MainMenu {
        ShowMyAccounts("1", "SHOWMYACCOUNTS"),
        NewAccount("2", "NEWACCOUNT"),
        Move("3", "MOVE"),
        Pay("4","PAY"),
        CustomerDetails("5", "CUSTOMERDETAILS"),
        Quit("9", "QUIT");

        private final String menuOptionId;
        private final String menuOption;

        MainMenu(String menuOptionId, String menuOption) {
            this.menuOptionId = menuOptionId;
            this.menuOption = menuOption;
        }

        public String getMenuOptionId() { return menuOptionId; }
        public String getMenuOption() { return menuOption; }

        public static void processMenuSelection(PrintWriter out) {
            // Print values
            for (MainMenu key : MainMenu.values()) {
                out.println(key.menuOptionId + ".\t" + key);
            }
        }
    }

