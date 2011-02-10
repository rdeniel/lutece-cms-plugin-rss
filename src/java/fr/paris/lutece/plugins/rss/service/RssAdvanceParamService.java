package fr.paris.lutece.plugins.rss.service;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.resource.ResourceService;

public class RssAdvanceParamService extends ResourceService {
    private static final String PROPERTY_NAME = "rss.param.service.name";
    private static final String PROPERTY_CACHE = "rss.param.service.cache";
    private static final String PROPERTY_LOADERS = "rss.param.service.loaders";
    
    private static RssAdvanceParamService _singleton = new RssAdvanceParamService(  );
    
    /** Creates a new instance of AgendaService */
    private RssAdvanceParamService(  )
    {
        super(  );
        setNameKey( PROPERTY_NAME );
        setCacheKey( PROPERTY_CACHE );
    }
    
	@Override
	protected String getLoadersProperty() {
		// TODO Auto-generated method stub
		return null;
	}
	
    /**
     * Returns the instance of the singleton
     *
     * @return The instance of the singleton
     */
    public static RssAdvanceParamService getInstance(  )
    {
        return _singleton;
    }
    
    public Map<String, Object> getManageAdvancedParameters( AdminUser user )
    {
    	Map<String, Object> model = new HashMap<String, Object>(  );
    	return model;
    }

}
