/*
 *  MIT License
 *
 *  Copyright (c) 2021 Toadless @ toaddev.net
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of Lavalite and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of Lavalite, and to permit persons to whom Lavalite is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Lavalite.
 *
 * LAVALITE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE
 */

package net.toaddev.lavalite.util;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;

public class FileUtil
{
    private FileUtil()
    {
        //Overrides the default, public, constructor
    }

    public static InputStream getResourceFile(String fileName)
    {
        InputStream file;
        try
        {
            file = FileUtil.class.getClassLoader().getResourceAsStream(fileName);
        }
        catch(Exception exception)
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
            while((str = reader.readLine()) != null)
            {
                stringBuilder.append(str + "\n");
            }
        }
        catch(Exception exception)
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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getXmlVal(Document xmlDoc, String xmlVal)
    {
        return xmlDoc.getElementsByTagName(xmlVal).item(0).getFirstChild().getNodeValue();
    }
}