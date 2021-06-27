package net.toaddev.snowball.gui;

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class TextAreaOutputStream extends OutputStream {
    private byte[]                          oneByte;
    private Appender                        appender;

    public TextAreaOutputStream(JTextArea txtara) {
        this(txtara,1000);
    }

    public TextAreaOutputStream(JTextArea txtara, int maxlin) {
        if(maxlin<1) { throw new IllegalArgumentException("TextAreaOutputStream maximum lines must be positive (value="+maxlin+")"); }
        oneByte=new byte[1];
        appender=new Appender(txtara,maxlin);
    }

    public synchronized void clear() {
        if(appender!=null) { appender.clear(); }
    }

    @Override
    public synchronized void close() {
        appender=null;
    }

    @Override
    public synchronized void flush() {
    }

    @Override
    public synchronized void write(int val) {
        oneByte[0]=(byte)val;
        write(oneByte,0,1);
    }

    @Override
    public synchronized void write(byte[] ba) {
        write(ba,0,ba.length);
    }

    @Override
    public synchronized void write(byte[] ba,int str,int len) {
        if(appender!=null) { appender.append(bytesToString(ba,str,len)); }
    }

    static private String bytesToString(byte[] ba, int str, int len) {
        try {
            return new String(ba,str,len,"UTF-8");
        } catch(UnsupportedEncodingException thr) {
            return new String(ba,str,len);
        }
    }

    static class Appender
            implements Runnable
    {
        static private final String         EOL1="\n";
        static private final String         EOL2=System.getProperty("line.separator",EOL1);

        private final JTextArea             textArea;
        private final int                   maxLines;                                                   // maximum lines allowed in text area
        private final LinkedList<Integer>   lengths;                                                    // length of lines within text area
        private final List<String>          values;                                                     // values waiting to be appended

        private int                         curLength;                                                  // length of current line
        private boolean                     clear;
        private boolean                     queue;

        Appender(JTextArea txtara, int maxlin) {
            textArea =txtara;
            maxLines =maxlin;
            lengths  =new LinkedList<>();
            values   =new ArrayList<>();

            curLength=0;
            clear    =false;
            queue    =true;
        }

        private synchronized void append(String val) {
            values.add(val);
            if(queue) {
                queue=false;
                EventQueue.invokeLater(this);
            }
        }

        private synchronized void clear() {
            clear=true;
            curLength=0;
            lengths.clear();
            values.clear();
            if(queue) {
                queue=false;
                EventQueue.invokeLater(this);
            }
        }

        @Override
        public synchronized void run() {
            if(clear) { textArea.setText(""); }
            values.stream().map((val) -> {
                curLength+=val.length();
                return val;
            }).map((val) -> {
                if(val.endsWith(EOL1) || val.endsWith(EOL2)) {
                    if(lengths.size()>=maxLines) { textArea.replaceRange("",0,lengths.removeFirst()); }
                    lengths.addLast(curLength);
                    curLength=0;
                }
                return val;
            }).forEach((val) -> {
                final String valNC =
                        val.replaceAll("\u001B\\[[;\\d]*m", "");
                textArea.append(valNC);
            });
            values.clear();
            clear = false;
            queue = true;
        }
    }
}

