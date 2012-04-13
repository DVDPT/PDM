package pt.isel.adeetc.meic.pdm.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

public final class IntentHelper
{
    private IntentHelper(){}

    public static Intent createIntentToReorderToFrontActivity(Context ctx, Class<? extends Activity> activityClass)
    {
        Intent intent = new Intent(ctx,activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        return intent;

    }
}
