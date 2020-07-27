package newbank.server;

import java.io.PrintWriter;

import static newbank.database.static_data.NewBankData.*;

public enum MainMenu {
        ShowMyAccounts(enumIdOne, showMyAccounts),
        NewAccount(enumIdTwo, newAccount),
        Move(enumIdThree, move),
        Pay(enumIdFour,pay),
        CustomerDetails(enumIdFive, customerDetail),
        Quit(enumIdNine, quit);

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

