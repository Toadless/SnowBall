/*
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package net.toaddev.snowball.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class IOUtil
{
    private static final Logger LOG = LoggerFactory.getLogger(IOUtil.class);

    private IOUtil()
    {
        //Overrides the default, public, constructor
    }

    public static InputStream getResourceFile(String fileName)
    {
        InputStream file;
        try
        {
            file = IOUtil.class.getClassLoader().getResourceAsStream(fileName);
        } catch (Exception exception)
        {
            return null;
        }
        return file;
    }

    public static String convertToString(InputStream inputStream)
    {
        InputStreamReader isReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(isReader);

        StringBuilder stringBuilder = new StringBuilder();
        String str;
        try
        {
            while ((str = reader.readLine()) != null)
            {
                stringBuilder.append(str + "\n");
            }
        } catch (Exception exception)
        {
            return "";
        }

        return stringBuilder.toString();
    }

    public static String getResourceFileContents(String name)
    {
        InputStream file = getResourceFile(name);
        return convertToString(file);
    }

    public static Document convertStringToXMLDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(new StringReader(xmlString)));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getXmlVal(Document xmlDoc, String xmlVal)
    {
        return xmlDoc.getElementsByTagName(xmlVal).item(0).getFirstChild().getNodeValue();
    }

    public static List<String> loadMessageFile(String fileName)
    {
        var file = new File("messages/" + fileName + "_messages.txt");
        try
        {
            InputStream inputStream = new FileInputStream(file);
            var reader = new BufferedReader(new InputStreamReader((inputStream), StandardCharsets.UTF_8));
            List<String> set = new ArrayList<>();
            try
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    set.add(line);
                }
                reader.close();
            } catch (IOException e)
            {
                LOG.error("Error reading message file", e);
            }
            return set;
        } catch (FileNotFoundException e)
        {
            LOG.error("Message file not found");
            return Collections.emptyList();
        }
    }
}