package pt.isel.adeetc.meic.pdm.services;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import pt.isel.adeetc.meic.pdm.common.StringHelper;

public class SimpleEmailSender implements IEmailSender
{
    private final Context _ctx;

    public SimpleEmailSender(Context ctx)
    {

        _ctx = ctx;
    }

    @Override
    public void sendEmail(String to, String subject, String message)
    {
        if (StringHelper.isNullOrEmpty(to))
            to = "";
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, message);
        intent.setData(Uri.parse(String.format("mailto:%s", to))); // or just "mailto:" for blank
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        _ctx.startActivity(intent);
    }
}
