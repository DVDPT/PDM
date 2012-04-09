package pt.isel.adeetc.meic.pdm.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;
import pt.isel.adeetc.meic.pdm.extensions.BaseApplication;

public final class UiHelper
{
    private UiHelper()
    {
    }

    public static void showToast(int stringId)
    {
        Toast.makeText
                (
                        BaseApplication.getContext(),
                        stringId,
                        Toast.LENGTH_LONG
                ).show();
    }

    @Deprecated
    public static void showToast(String str)
    {
        Toast.makeText
                (
                        BaseApplication.getContext(),
                        str,
                        Toast.LENGTH_LONG
                ).show();
    }


}
