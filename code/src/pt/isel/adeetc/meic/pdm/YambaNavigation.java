package pt.isel.adeetc.meic.pdm;

import pt.isel.adeetc.meic.pdm.extensions.BoundedService;

public final class YambaNavigation
{

    private YambaNavigation()
    {
    }

    public static final String STATUS_SERVICE_MESSAGE_PARAM_NAME = "STATUS_SERVICE_MESSAGE_PARAM_NAME";
    public static final String STATUS_SERVICE_RESULT_PARAM_NAME = BoundedService.SERVICE_STATUS_RESPONSE_PARAM_NAME;
    public static final String STATUS_SERVICE_ERROR_PARAM_NAME = BoundedService.SERVICE_ERROR_RESPONSE_PARAM_NAME;

    public static final String TIMELINE_SERVICE_RESULT_PARAM_NAME = BoundedService.SERVICE_STATUS_RESPONSE_PARAM_NAME;
    public static final String TIMELINE_SERVICE_ERROR_PARAM_NAME = BoundedService.SERVICE_ERROR_RESPONSE_PARAM_NAME;
    public static final String TIMELINE_TO_STATUS_DETAILS_PARAM_NAME = "statusDetailsParam";
    public static final String USER_INFO_SERVICE_PARAM_NAME = "UserInfoServiceParamName";
}
