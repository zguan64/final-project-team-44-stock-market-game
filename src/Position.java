import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class Position {

	private String symbol;
	private double shares;
	private double averageCost;
	private double lastPrice;
	private double costBasis;
	private double currentValue;
	private double positionReturn;
	
	/**
	 * Constructs a position object to be used in other classes
	 * a position is equivalent to a single stock
	 * @param currSymbol
	 * @param currShares
	 * @param currAverageCost
	 */
	public Position(String currSymbol, double currShares, double currAverageCost) {
		YahooQuote quote = new YahooQuote();
		symbol = currSymbol.toUpperCase();
		shares = currShares;
		//USDCASH is the actual cash position, checking and setting cost to 1
		//which equals one US dollar
		if (symbol.equals("USDCASH")) {
			averageCost = 1.0;
		}
		else {
			averageCost = currAverageCost;
		}
		
		//Last price is grabbed from the YahooQuote class to the url endpoint based on the position's symbol.
		//The last price grabbed is used to calculate current value and position return. The catch cannot be accessed
		//via running the program as we run getValidSymbol before any of this can be called. thus, the program cannot natively
		//access and through the IOException being caught in line 46.
		
		if (symbol.equals("USDCASH")) {
			lastPrice = 1.0;
		}
		else {
			try {
				lastPrice = Double.parseDouble(quote.getField(symbol, "regularMarketPrice\":(.+?),", "chart"));
			} catch (IOException e) {
				System.out.println("Could not get the stock price. The stock does not exist or does not trade.");
//				e.printStackTrace();
			}
		}
		
		costBasis = shares * averageCost;
		currentValue = shares * lastPrice;
		//position return will be calculated to the nearest one hundredth of a percentage point.
		positionReturn = (currentValue / costBasis - 1) * 100;
	}
	
	/**
	 * returns the stock symbol of the position object
	 * @return
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * returns the number of shares of a stock from the position object
	 * @return
	 */
	public double getShares() {
		return shares;
	}
	
	/**
	 * returns the average cost of a stock from the position object
	 * @return
	 */
	public double getAverageCost() {
		return averageCost;
	}

	/**
	 * returns the last price of a stock from the position object
	 * @return
	 */
	public double getLastPrice() {
		return lastPrice;
	}

	/**
	 * returns the cost basis of a stock from the position object
	 * cost basis = total price you paid for all shares of a particular stock
	 * @return
	 */
	public double getCostBasis() {
		return costBasis;
	}

	/**
	 * returns the current value of a stock from the position object
	 * current value = last price * number of shares
	 * @return
	 */
	public double getCurrentValue() {
		return currentValue;
	}

	/**
	 * returns the return on a stock from the position object
	 * return = (current value / cost basis) minus one
	 * @return
	 */
	public double getPositionReturn() {
		return positionReturn;
	}

	@Override
	/**
	 * helper method to format the position object 
	 * when printing to the console
	 */
	public String toString() {		
		NumberFormat dollarFormat = NumberFormat.getCurrencyInstance(Locale.US);
		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
		String sharesOutput =  numberFormat.format(getShares());
        String avgCostOutput = dollarFormat.format(getAverageCost());
        String lastPriceOutput = dollarFormat.format(getLastPrice());
        String costBasisOutput = dollarFormat.format(getCostBasis());
        String currentValueOutput = dollarFormat.format(getCurrentValue());
        String returnValueOutput = String.format("%,.2f", getPositionReturn());
		return (String.format("%10s",symbol) 
				+ "\t"  + String.format("%13s",sharesOutput) 
				+ "\t" + String.format("%13s",avgCostOutput) 
				+ "\t"+ String.format("%13s",lastPriceOutput) 
				+ "\t" + String.format("%18s", costBasisOutput) 
				+ "\t" + String.format("%18s", currentValueOutput) 
				+ "\t" + String.format("%10s",returnValueOutput));
	}
	
}
