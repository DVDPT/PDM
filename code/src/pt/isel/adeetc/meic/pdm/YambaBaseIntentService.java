package pt.isel.adeetc.meic.pdm;

import pt.isel.adeetc.meic.pdm.extensions.BaseIntentService;

/**
 * Created by IntelliJ IDEA.
 * User: Sorcha
 * Date: 07-06-2012
 * Time: 13:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class YambaBaseIntentService extends BaseIntentService<YambaApplication>{
    public YambaBaseIntentService(String name) {
        super(name);
    }
}
