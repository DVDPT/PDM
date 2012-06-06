package pt.isel.adeetc.meic.pdm.common.db;

public final class DbSetOnCompletedOperationEventData
{
    private final int _operation;
    private Object _data;

    public int getOperation()
    {
        return _operation;
    }

    public Object getData()
    {
        return _data;
    }

    public DbSetOnCompletedOperationEventData(int operation)
    {
        this._operation = operation;
    }

    public void setData(Object data)
    {
        _data = data;
    }
}
