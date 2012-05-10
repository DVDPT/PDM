package pt.isel.adeetc.meic.pdm.common;

public interface Action<T>
{
    void perform(T t);
}
