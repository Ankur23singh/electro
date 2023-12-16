package electro.service;

import electro.model.Portfolio;

public class UserPortfolioResponse {
    private String message;
    private Portfolio portfolio;

    public UserPortfolioResponse(String message) {
        this.message = message;
        this.portfolio = portfolio;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }


}
