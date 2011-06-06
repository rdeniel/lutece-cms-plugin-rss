/*
 * Copyright (c) 2002-2011, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.rss.business.resourceRss;

import fr.paris.lutece.portal.business.rss.IResourceRss;
import fr.paris.lutece.portal.business.rss.IResourceRssType;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.util.AppLogService;

import java.util.Collection;


/**
 * ResourceRssFactory
 */
public class ResourceRssFactory implements IResourceRssFactory
{
    /**
     * 
     * {@inheritDoc}
     */
    public IResourceRss getResourceRss( String strKey )
    {
    	if ( strKey == null )
    	{
    		return null;
    	}
    	
        IResourceRss resourceRss = null;
        IResourceRssType resourceRssType = null;
        
        Collection<IResourceRssType> listResourceRssType = SpringContextService.getBeansOfType( IResourceRssType.class );
        
        for ( IResourceRssType rssType : listResourceRssType )
        {
        	if ( strKey.equals( rssType.getKey(  ) ) )
        	{
        		resourceRssType = rssType;
        		break;
        	}
        }
        
        if ( resourceRssType != null )
        {
            try
            {
                resourceRss = (IResourceRss) Class.forName( resourceRssType.getClassName(  ) ).newInstance(  );
                resourceRss.setResourceRssType( resourceRssType );
            }
            catch ( ClassNotFoundException e )
            {
                // class doesn't exist
                AppLogService.error( e );
            }
            catch ( InstantiationException e )
            {
                // Class is abstract or is an interface or haven't accessible
                // builder
                AppLogService.error( e );
            }
            catch ( IllegalAccessException e )
            {
                // can't access to rhe class
                AppLogService.error( e );
            }
        }

        return resourceRss;
    }

    /**
     * 
     * {@inheritDoc}
     */
    public Collection<IResourceRssType> getAllResourceRssType(  )
    {
        return SpringContextService.getBeansOfType( IResourceRssType.class );
    }
}
