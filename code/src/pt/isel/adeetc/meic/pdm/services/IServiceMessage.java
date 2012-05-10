package pt.isel.adeetc.meic.pdm.services;

import pt.isel.adeetc.meic.pdm.common.IEventHandler;

public interface IServiceMessage <T,Q>
{
    T getData();
    IEventHandler<Q> getCallback();
}
