package pt.isel.adeetc.meic.pdm.services;

import android.content.Intent;
import android.os.IBinder;
import android.os.Message;
import pt.isel.adeetc.meic.pdm.YambaApplication;
import pt.isel.adeetc.meic.pdm.YambaBaseService;
import pt.isel.adeetc.meic.pdm.YambaNavigation;
import pt.isel.adeetc.meic.pdm.extensions.BoundedService;
import winterwell.jtwitter.Twitter;

public class UserInfoPullService extends YambaBaseService
{

    private final BoundedService _boundedImpl = new BoundedService(((YambaApplication)YambaApplication.getInstance()).getCustomHandlerThread().getLooper())
    {
        @Override
        protected int handleClientRequest(Message cliengMsg, Message serviceResponse) throws Exception
        {
            Twitter t = getApplicationInstance().getTwitterClient().getTwitter();
            try
            {
                Twitter.User user = t.getUser(getApplicationInstance().getUserName());
                YambaUserInfo userInfo = new YambaUserInfo(user);
                serviceResponse.getData().putParcelable(YambaNavigation.USER_INFO_SERVICE_RESPONSE_PARAM_NAME, userInfo);
                return BoundedService.SERVICE_RESPONSE_OK;
            } catch (Exception e)
            {
                e = getApplicationInstance().getTwitterClient().checkIfIsAuthenticationExceptionAndReplace(e);
                throw e;
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        return _boundedImpl.getBinder();
    }
}
