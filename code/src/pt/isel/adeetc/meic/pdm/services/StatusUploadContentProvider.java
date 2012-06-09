package pt.isel.adeetc.meic.pdm.services;

import android.content.*;
import android.database.CharArrayBuffer;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.common.CollectionCursor;
import pt.isel.adeetc.meic.pdm.exceptions.ShouldNotHappenException;

import java.io.*;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 09-06-2012
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */
public class StatusUploadContentProvider extends ContentProvider
{

    private static final String AUTHORITY = "pt.isel.adeetc.meic.pdm.status.contentprovider";

    private static final String BASE_PATH = "status";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private final String FILE_NAME="status.txt";
    public static final String KEY_VALUES_STATUS = "statusMessage";

    private File _statusFile;
    
    private Integer _countStatus = new Integer(0);


    @Override
    public boolean onCreate()
    {
        _statusFile = YambaApplication.getContext().getFileStreamPath(FILE_NAME);

        FileOutputStream fileOutput;

        DataOutputStream stream;

        DataInputStream  outData;
        try {
            if(!_statusFile.exists())
            {
            
                //_statusFile = new File(YambaApplication.getContext().getFilesDir(), FILE_NAME);
                _statusFile.createNewFile();
                fileOutput = new FileOutputStream(_statusFile);
                stream = new DataOutputStream(fileOutput);
                 stream.write(_countStatus);
                stream.writeChars("\n");
                stream.flush();  
            }
            else
            {
                outData = new DataInputStream (new FileInputStream(_statusFile));
                _countStatus = outData.readInt();
            }
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) 
    {
        DataInputStream outData;
        LinkedList<String> messages = new LinkedList<String>();
        String message;
        try {
            
            outData = new DataInputStream (new FileInputStream(_statusFile));
            outData.readLine();

            while((message=outData.readLine())!= null)
            {
                messages.add(message);
            }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return new CollectionCursor(messages);

    }

    @Override
    public String getType(Uri uri) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int id = 0;
        BufferedOutputStream stream;
        
        try {

            stream = new BufferedOutputStream(new FileOutputStream(_statusFile));

            String status = String.format("%s\n", contentValues.getAsString(KEY_VALUES_STATUS));

            stream.write(status.getBytes());

            id = _countStatus++;

            stream.write(_countStatus.toString().getBytes(),0,_countStatus.toString().length());

        } catch (Exception e) {
           //
        }
        return ContentUris.withAppendedId(uri,id);

    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {

        BufferedReader reader;
        FileWriter writer;
        StringBuffer stringBuffer;
        String message;
        boolean deleteLine=false;
        try {

            reader = new BufferedReader(new InputStreamReader(new FileInputStream(_statusFile)));

            writer = new FileWriter(_statusFile,false);

            stringBuffer = new StringBuffer();

            while((message=reader.readLine())!=null)
            {
                for(int i=0; i<strings.length;i++)
                {
                    if(message.equals(strings[i]))
                    {
                       deleteLine=true;
                       --_countStatus;
                        break;
                    }
                }
                if(deleteLine)
                {
                    stringBuffer.append(String.format("%s \n",message));
                    deleteLine=false;
                }
            }

            StringBuffer aux = new StringBuffer(String.format("%d \n %s", _countStatus,stringBuffer.toString()));

            writer.write(aux.toString(), 0, aux.length());

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return strings.length ;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new ShouldNotHappenException();
    }

}
