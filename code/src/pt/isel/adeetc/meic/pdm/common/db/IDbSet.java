package pt.isel.adeetc.meic.pdm.common.db;

import pt.isel.adeetc.meic.pdm.common.IEvent;

public interface IDbSet<T> extends Iterable<T>
{
    static int DBSET_OPERATION_ADD = 1;
    static int DBSET_OPERATION_REMOVE = 2;
    static int DBSET_OPERATION_CLEAR_ALL = 3;

    T create();
    IEvent getOnOperationCompleted();

    boolean add(T t);
    void addAsync(T t);

    boolean remove(T t);
    void removeAsync(T t);

    boolean clearAll();
    void clearAllAsync();



}
