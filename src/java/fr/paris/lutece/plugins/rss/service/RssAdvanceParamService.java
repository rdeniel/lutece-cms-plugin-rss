package fr.paris.lutece.plugins.rss.service;

import java.util.HashMap;
import java.util.Map;

import fr.paris.lutece.plugins.rss.business.parameter.RssFeedParameterHome;
import fr.paris.lutece.portal.business.user.AdminUser;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.resource.ResourceService;
import fr.paris.lutece.util.ReferenceItem;

public class RssAdvanceParamService extends ResourceService
{
	private static final String MARK_LOCALE = "locale";
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
    
    /**
     * Build the model for the advanced parameters page
     * @param user the admin user who tries to access the page
     * @return the model for the page
     */
    public Map<String, Object> getManageAdvancedParameters( AdminUser user )
    {
    	Map<String, Object> model = new HashMap<String, Object>(  );
    	for( ReferenceItem param : RssFeedParameterHome.findAll( PluginService.getPlugin( RssPlugin.PLUGIN_NAME ) ) )
        {
        	model.put( param.getCode(  ), param.getName(  ) );
        }
    	model.put( MARK_LOCALE, user.getLocale(  ) );
    	return model;
    }

}
