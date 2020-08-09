package newbank.server;

import java.io.PrintWriter;
import static newbank.database.static_data.NewBankData.*;

public enum InitialMenu {
        Login(enumIdOne, updateCustomerName),
        RegisterNewCustomer(enumIdTwo, updateCustomerEmail),
        Quit(enumIdNine, quit);

        private final String menuOptionId;
        private final String menuOption;

        InitialMenu(String menuOptionId, String menuOption) {
            this.menuOptionId = menuOptionId;
            this.menuOption = menuOption;
        }

        public String getMenuOptionId() { return menuOptionId; }
        public String getMenuOption() { return menuOption; }

        public static void processMenuSelection(PrintWriter out) {
            // Print values
            for (InitialMenu key : InitialMenu.values()) {
                out.println(key.menuOptionId + ".\t" + key);
            }
        }
    }

