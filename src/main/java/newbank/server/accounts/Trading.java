package newbank.server.accounts;

import newbank.server.Account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Trading extends Account {

    private Risk risk;
    private Portfolio portfolio;

    public Trading(String accountName, double openingBalance, int accountPin) {
        super(accountName, openingBalance, accountPin);
    }

    public void SetRisk(Risk riskLevel) {
        this.risk = riskLevel;
    }

    public Risk GetRisk() {
        return this.risk;
    }

    public void BuyStock(String code, double price, int shares) {
        double totalCost = price * shares;

        if (totalCost >= super.getBalance()) {
            portfolio.addNewStock(new Stock(code, price, shares));
            super.withdrawMoney(totalCost);
        }
    }

    public Portfolio getPortfolio() {
        return this.portfolio;
    }

    private static class Portfolio {
        private List<Stock> stocks;

        public Portfolio() {
            this.stocks = new ArrayList<>();
        }

        public List<Stock> getPortfolio() {
            return this.stocks;
        }

        public double getTotalPortfolioPurchaseValue() {
            double total = 0;
            for (Stock stock : stocks) {
                total += (stock.getPurchasePrice() * stock.getSharesOwned());
            }
            return total;
        }

        public double getTotalPortfolioCurrentValue() {
            double total = 0;
            for (Stock stock : stocks) {
                total += (stock.getCurrentPrice() * stock.getSharesOwned());
            }
            return total;
        }

        public void addNewStock(Stock stock) {
            stocks.add(stock);
        }
    }

    private static class Stock {
        private String companyCode;
        private double purchasePrice;
        private int sharesOwned;

        public LocalDateTime datePurchased;

        public Stock(String companyCode, double purchasePrice, int sharesOwned) {
            this.companyCode = companyCode;
            this.purchasePrice = purchasePrice;
            this.sharesOwned = sharesOwned;
            this.datePurchased = LocalDateTime.now();
        }

        public String getCompanyCode() {
            return this.companyCode;
        }

        public double getPurchasePrice() {
            return this.purchasePrice;
        }

        // TODO: use of API or separate service where this data can come from
        public double getCurrentPrice() {
            return 1;
        }

        public int getSharesOwned() {
            return this.sharesOwned;
        }

        public double getNetValue() {
            return (getCurrentPrice() * this.sharesOwned) - (getPurchasePrice() * this.sharesOwned);
        }

        /* Used to update price per share to reflect accurate total value of owned stock */
        public void setPrice(double price) {
            this.purchasePrice = price;
        }

        @Override
        public String toString() {
            return "Stock{" +
                    "code='" + companyCode + '\'' +
                    ", price='" + purchasePrice + '\'' +
                    ", shares=" + sharesOwned +
                    '}';
        }
    }

    private enum Risk {
        VERY_HIGH,
        HIGH,
        BALANCED,
        LOW,
        VERY_LOW
    }
}
