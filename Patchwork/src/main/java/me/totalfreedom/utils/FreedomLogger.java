package me.totalfreedom.utils;

import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class FreedomLogger
{
    private final Logger logger;
    private boolean debug = false;

    private FreedomLogger(String moduleName)
    {
        this.logger = LoggerFactory.getLogger("FreedomNetworkSuite::" + moduleName);
    }

    public static FreedomLogger getLogger(String moduleName)
    {
        return new FreedomLogger(moduleName);
    }

    public void setDebugMode(boolean debug)
    {
        this.debug = debug;
    }

    /**
     * This method allows you to log a message to the console.
     *
     * @param message The message to send.
     */
    public void info(String message)
    {
        logger.info(message);
    }

    /**
     * This method allows you to log a component to the console.
     *
     * @param component The component to send.
     * @return A plain text representation of the message
     */
    public String infoComponent(Component component)
    {
        String plainText = FreedomAdventure.toPlainText(component);

        logger.info(plainText);
        return plainText;
    }

    /**
     * This method allows you to log a message to the console,
     * while also returning a Component that could be used to
     * message a player.
     *
     * @param message The message to send.
     * @return A component representation of the message.
     */
    public Component info(Supplier<String> message)
    {
        logger.info(message.get());
        return Component.text(message.get());
    }

    /**
     * This method allows you to log a component to the console,
     * while also returning a String representation of the
     * component
     *
     * @param component The component to send.
     * @return A string representation of the message.
     */
    public String infoComponent(Supplier<Component> component)
    {
        return this.infoComponent(component.get());
    }

    /**
     * This method allows you to log a warning to the console.
     *
     * @param message The message to send.
     */
    public void warn(String message)
    {
        logger.warn(message);
    }

    /**
     * This method allows you to log a warning to the console.
     *
     * @param component The component to send.
     */
    public void warnComponent(Component component)
    {
        String plainText = FreedomAdventure.toPlainText(component);

        logger.warn(plainText);
    }

    /**
     * This method logs an error message to the console.
     * It is highly recommended to deconstruct the stack trace and pass it
     * in a more readable format to this method.
     *
     * @param message The message to send.
     */
    public void error(String message)
    {
        logger.error(message);
    }

    /**
     * This method logs an error component to the console.
     *
     * @param component The message to send.
     */
    public String errorComponent(Component component)
    {
        String plainText = FreedomAdventure.toPlainText(component);

        logger.error(plainText);

        return plainText;
    }

    /**
     * This method allows you to log an exception directly to the console.
     *
     * @param th The exception to log.
     */
    public void error(Throwable th)
    {
        logger.error("An error occurred:\n", th);
    }

    /**
     * This method allows you to log an error message to the console,
     * while also returning a Component that could be used to
     * message a player. It is highly recommended that you deconstruct and limit
     * the stack trace before passing it to this method, if you are intending to
     * use it for player communication.
     *
     * @param message The message to send.
     * @return A component representation of the message.
     */
    public Component error(Supplier<String> message)
    {
        logger.error(message.get());
        return Component.text(message.get());
    }

    /**
     * This method allows you to log an error component to the console,
     * while also returning a String representation of the error
     * component.
     *
     * @param component The component to send.
     * @return A String representation of the component.
     */
    public String errorComponent(Supplier<Component> component)
    {
        return this.errorComponent(component.get());
    }

    /**
     * This method allows you to log a debug message to the console.
     * This method will only log if debug mode is enabled.
     *
     * @param message The message to send.
     */
    public void debug(String message)
    {
        if (debug)
            logger.debug(message);
    }

    /**
     * This method allows you to log a debug component to the console.
     * This method will only log if debug mode is enabled.
     *
     * @param component The component to send.
     */
    public String debugComponent(Component component)
    {
        String plainText = FreedomAdventure.toPlainText(component);

        this.debug(plainText);

        return plainText;
    }

    /**
     * This method allows you to log a debug message to the console,
     * while also returning a Component that could be used to
     * message a player. This method will only log if debug mode is enabled.
     * If debug mode is not enabled, this method will return an empty component.
     *
     * @param message The message to send.
     * @return A component representation of the message.
     */
    public Component debug(Supplier<String> message)
    {
        if (debug)
        {
            logger.debug(message.get());
            return Component.text(message.get());
        }
        return Component.empty();
    }

    /**
     * This method allows you to log a debug component to the console,
     * while also returning a String representation of the debug component.
     *
     * @param component The component to send.
     * @return A String representation of the message.
     */
    public String debugComponent(Supplier<Component> component)
    {
        if (debug)
        {
            return this.debugComponent(component.get());
        }
        return "";
    }

}
