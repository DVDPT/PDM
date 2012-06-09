package pt.isel.adeetc.meic.pdm.common;

import android.content.ContentResolver;
import android.database.*;
import android.net.Uri;
import android.os.Bundle;
import com.google.common.collect.ImmutableCollection;

import java.security.InvalidParameterException;
import java.util.Collection;
import java.util.Iterator;


public class CollectionCursor<T> implements Cursor{

    private Collection<T> _collection;
    private int _position=-1;
    private T _current;
    private T[] _array;
    private boolean _isClosed;


    public CollectionCursor(Collection<T> collection){
        _collection=collection;
        _array=_collection.toArray(_array);

    }

    @Override
    public int getCount() {
       return _collection.size();
    }

    @Override
    public int getPosition() {
        return _position;
    }

    @Override
    public boolean move(int i) {
        
        int newPosition = _position+i;
        if(newPosition >_collection.size() || newPosition < 0)
        {
            return false;
        }
        _position += newPosition;
        return true;
    }

    @Override
    public boolean moveToPosition(int i) {
        
        if(i > _collection.size() || i<0)
            return false;
        _position=i;
        return true;
    }

    @Override
    public boolean moveToFirst() {
        _position=0;
        return true;
    }

    @Override
    public boolean moveToLast() {
        
        _position = _collection.size() -1;
        return true;
    }

    @Override
    public boolean moveToNext() {

        if(_position >= _collection.size()-1)
            return false;
        if(_position!=-1){
            _position++;

        }else{
            _position=0;
        }

        return true;
    }

    @Override
    public boolean moveToPrevious() {
        
        if(_position<=0)
            return false;
        _position--;
        return true;
    }

    @Override
    public boolean isFirst() {

        return _position==0;
    }

    @Override
    public boolean isLast() {
        return _position==(_collection.size()-1);
    }

    @Override
    public boolean isBeforeFirst() {

        return _position==1;
    }

    @Override
    public boolean isAfterLast() {
        return _position == _collection.size()-2;
    }

    @Override
    public int getColumnIndex(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnIndexOrThrow(String s) throws IllegalArgumentException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getColumnName(int i) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getColumnNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException();
    }

    @Override
    public byte[] getBlob(int i) {
        return new byte[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getString(int i) {
        return  _array[_position].toString();
    }

    @Override
    public void copyStringToBuffer(int i, CharArrayBuffer charArrayBuffer)
    {
        throw new UnsupportedOperationException();

    }

    @Override
    public short getShort(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getInt(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public long getLong(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public float getFloat(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public double getDouble(int i) {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean isNull(int i) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void deactivate() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean requery() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void close() {
        _isClosed=true;
    }

    @Override
    public boolean isClosed() {
        return _isClosed;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerContentObserver(ContentObserver contentObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterContentObserver(ContentObserver contentObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setNotificationUri(ContentResolver contentResolver, Uri uri) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean getWantsAllOnMoveCalls() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle getExtras() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bundle respond(Bundle bundle) {
       throw new UnsupportedOperationException();
    }
}
