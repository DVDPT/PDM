package pt.isel.adeetc.meic.pdm.common.db;

public interface IDbSet<T> extends Iterable<T>
{
    T create();
    boolean add(T t);
    boolean remote(T t);
    boolean clearAll();

}
