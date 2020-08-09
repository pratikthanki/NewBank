package newbank.server;

import java.io.PrintWriter;
import static newbank.database.static_data.NewBankData.*;

public enum UpdateCustomerDetailsMenu {
        UpdateCustomerName(enumIdOne, updateCustomerName),
        UpdateCustomerEmail(enumIdTwo, updateCustomerEmail),
        UpdateCustomerAddress(enumIdThree, updateCustomerAddress),
        UpdateCustomerDOB(enumIdFour, updateCustomerDOB),
        Quit(enumIdNine, quit);

        private final String menuOptionId;
        private final String menuOption;

        UpdateCustomerDetailsMenu(String menuOptionId, String menuOption) {
            this.menuOptionId = menuOptionId;
            this.menuOption = menuOption;
        }

        public String getMenuOptionId() { return menuOptionId; }
        public String getMenuOption() { return menuOption; }

        public static void processMenuSelection(PrintWriter out) {
            // Print values
            for (UpdateCustomerDetailsMenu key : UpdateCustomerDetailsMenu.values()) {
                out.println(key.menuOptionId + ".\t" + key);
            }
        }
    }

