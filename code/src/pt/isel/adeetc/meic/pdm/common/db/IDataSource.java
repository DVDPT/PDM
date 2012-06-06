package pt.isel.adeetc.meic.pdm.common.db;

public interface IDataSource
{
    void open();
    boolean isOpen();
    void close();
}
