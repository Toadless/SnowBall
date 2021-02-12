package net.toaddev.snowball.entities;

import net.toaddev.snowball.main.BotController;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class BotControllerBuilder
{
    public Document xmlDoc;

    public BotControllerBuilder()
    {

    }

    public BotControllerBuilder setXmlDoc(Document xmlDoc)
    {
        this.xmlDoc = xmlDoc;

        return this;
    }

    public void build() throws ParserConfigurationException, SAXException, IOException
    {
        new BotController(xmlDoc);
    }
}