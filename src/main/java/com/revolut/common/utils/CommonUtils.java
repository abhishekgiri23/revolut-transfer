package com.revolut.common.utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Properties;

public class CommonUtils {
    
    private static Properties PROPERTIES = new Properties();
    public static final BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
    
    
    static Logger LOG = Logger.getLogger(CommonUtils.class);
    
    static {
        loadConfig("app.properties");
    }
    
    /**
     * Load the config vale from property file
     * @param fileName
     */
    public static void loadConfig(String fileName) {
        if (fileName == null) {
            LOG.warn("loadConfig: config file name cannot be null");
        } else {
            try {
                final InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
                PROPERTIES.load(fis);
                
            } catch (FileNotFoundException fne) {
                LOG.error("loadConfig(): file name not found " + fileName, fne);
            } catch (IOException ioe) {
                LOG.error("loadConfig(): file name not found " + fileName, ioe);
            }
        }
    }
    
    /**
     * get the property value.
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        String value = PROPERTIES.getProperty(key);
        if (value == null) {
            value = System.getProperty(key);
        }
        return value;
    }
    
    /**
     * validate the currency code.
     * @param inputCode
     * @return
     */
    public static boolean validateCurrencyCode(String inputCode) {
        try {
            Currency currency = Currency.getInstance(inputCode);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Validate Currency Code: " + currency.getSymbol());
            }
            return currency.getCurrencyCode().equals(inputCode);
        } catch (Exception e) {
            LOG.warn("Cannot parse the input Currency Code, Validation Failed: ", e);
        }
        return false;
    }
    
    
}
