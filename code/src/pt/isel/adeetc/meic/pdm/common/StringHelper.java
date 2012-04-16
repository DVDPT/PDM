package pt.isel.adeetc.meic.pdm.common;

public final class StringHelper
{
    private StringHelper(){}

    public static boolean isNullOrEmpty(String s)
    {
        return s == null || s.length() == 0;

    }
}
