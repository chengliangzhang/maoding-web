/*
* Copyright (C) 2008 Happy Fish / YuQing
*
* FastDFS Java Client may be copied only under the terms of the GNU Lesser
* General Public License (LGPL).
* Please visit the FastDFS Home Page http://www.csource.org/ for more detail.
*/

package com.maoding.core.component.fastdfs.common;

/**
* My Exception
* @author Happy Fish / YuQing
* @version Version 1.0
*/
public class FastDFSException extends Exception
{
	private static final long serialVersionUID = 1L;

	public FastDFSException()
    {
    }
    
    public FastDFSException(String message)
    {
    		super(message);
    }
}
