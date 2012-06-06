package pt.isel.adeetc.meic.pdm.exceptions;

public class ShouldNotHappenException extends RuntimeException
{
    public ShouldNotHappenException()
    {
    }

    public ShouldNotHappenException(String detailMessage)
    {
        super(detailMessage);
    }

    public ShouldNotHappenException(String detailMessage, Throwable throwable)
    {
        super(detailMessage, throwable);
    }

    public ShouldNotHappenException(Throwable throwable)
    {
        super(throwable);
    }
}
