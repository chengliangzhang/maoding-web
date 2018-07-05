package com.maoding.core.component.fastdfs.conn.pool;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

/** 
 *   <B>说       明</B>:表示fastdfs连接池对象。
 */
public class FastDFSConnectionPool extends GenericKeyedObjectPool<InetSocketAddress, ConnectionSupplier>{

	public FastDFSConnectionPool(
			KeyedPooledObjectFactory<InetSocketAddress, ConnectionSupplier> factory) {
		super(factory);
	}

	public FastDFSConnectionPool(
			KeyedPooledObjectFactory<InetSocketAddress, ConnectionSupplier> factory,
			GenericKeyedObjectPoolConfig config) {
		super(factory, config);
	}

	
}
 