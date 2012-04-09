package pt.isel.adeetc.meic.pdm.common.holders;

public class ViewHolder3<T1,T2,T3>
{
    private final T1 _t1;
    private final T2 _t2;
    private final T3 _t3;

    public ViewHolder3(T1 t1, T2 t2, T3 t3)
    {

        this._t1 = t1;
        this._t2 = t2;
        this._t3 = t3;
    }

    public T1 getT1(){
        return _t1;
    }

    public T2 getT2(){
        return _t2;
    }

    public T3 getT3(){
        return _t3;
    }
}
