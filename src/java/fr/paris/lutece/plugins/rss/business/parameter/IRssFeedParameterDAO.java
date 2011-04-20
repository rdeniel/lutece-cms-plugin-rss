package fr.paris.lutece.plugins.rss.business.parameter;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.ReferenceItem;
import fr.paris.lutece.util.ReferenceList;

/**
 * 
 * IRssFeedParameterDAO
 *
 */
public interface IRssFeedParameterDAO
{
	/**
     * Load the parameter value
     * @param strParameterKey the parameter key
     * @param plugin
     * @return The parameter value
     */
	ReferenceItem load(String strParameterKey, Plugin plugin);

	/**
     * Update the parameter value
     * @param strParameterValue The parameter value
     * @param strParameterKey The parameter key
     * @param plugin
     */
	void store(ReferenceItem param, Plugin plugin);

	/**
     * Load all the default values
     * @param plugin Plugin
     * @return a list of ReferenceItem
     */
	ReferenceList selectAll(Plugin plugin);

}