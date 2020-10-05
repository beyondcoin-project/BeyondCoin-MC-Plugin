package nugetzrul3.mc.tipbot.config;

import java.math.BigDecimal;

public class Constants {
    public String host = "127.0.0.1";
    public String user = "username";
    public String pass = "password";
    public String port = "10332";
    public String ticker = "BYND";
    public String coinName = "BeyondCoin";
    public int conf = 6;
    public String explorer = "https://chainz.cryptoid.info/bynd/tx.dws?";
    public BigDecimal withdraw_fee = new BigDecimal("0.001");
    public String price_url = "https://api.coingecko.com/api/v3/simple/price?ids=beyondcoin&vs_currencies=btc,usd";

}
