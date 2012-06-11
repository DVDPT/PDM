package pt.isel.adeetc.meic.pdm.services;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
    
    private static Integer _countStatus = new Integer(0);


    @Override
    public boolean onCreate()
    {
        _statusFile = YambaApplication.getContext().getFileStreamPath(FILE_NAME);

        try {
            if(!_statusFile.exists())
            {
               _statusFile.createNewFile();
            }
        } catch (IOException e) {
            throw new ShouldNotHappenException(e);
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

            while((message=outData.readLine())!= null)
            {
                messages.add(message);
            }
            outData.close();
        } catch (Exception e) {
            throw new ShouldNotHappenException(e);
        }


        return new CollectionCursor(messages);

    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        int id = 0;
        FileWriter stream;
        
        try {

            stream = new FileWriter(_statusFile,true);
            BufferedWriter bufferedWriter = new BufferedWriter(stream);

            bufferedWriter.write(contentValues.getAsString(KEY_VALUES_STATUS));
            bufferedWriter.newLine();

            bufferedWriter.close();

            stream.close();

            id = _countStatus++;
        } catch (Exception e) {
            throw new ShouldNotHappenException(e);
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
                if(!deleteLine)
                {
                    stringBuffer.append(String.format("%s \n",message));
                }
                deleteLine=false;
            }

            _statusFile.delete();
            _statusFile.createNewFile();

            writer = new FileWriter(_statusFile,true);

            if(stringBuffer.toString().length()>0)
            {
                writer.write(stringBuffer.toString());
                writer.flush();
            }

            writer.close();
            reader.close();


        } catch (Exception e) {
            throw new ShouldNotHappenException(e);
        }

        return strings.length ;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException();
    }
}
