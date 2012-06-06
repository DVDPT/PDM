package pt.isel.adeetc.meic.pdm.common.db;

public interface IDbSet<T>
{
    T create();
    boolean add(T t);
    void addAll(Iterable<T> t);
    Iterable<T> getAll();
    boolean remove(T t);
    boolean clearAll();
}
