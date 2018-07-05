package com.maoding.core.component.fastdfs.conn.pool;

import java.net.InetSocketAddress;

import org.apache.commons.pool2.impl.GenericKeyedObjectPool;

/** 
 *   <B>说       明</B>:表示默认fastdfs连接池工厂类。
 *
 */
public final class DefaultFastDFSConnectionPoolFactory {
	public static final DefaultFastDFSConnectionPoolFactory INSTANCE = new DefaultFastDFSConnectionPoolFactory();
	private final GenericKeyedObjectPool<InetSocketAddress, ConnectionSupplier> fastDFSConnectionPool;
	private DefaultFastDFSConnectionPoolFactory() {
		PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory();
		ConnectionPoolConfig connectionPoolConfig = new ConnectionPoolConfig();
		fastDFSConnectionPool = new FastDFSConnectionPool(pooledConnectionFactory,connectionPoolConfig);
	}
	
	public GenericKeyedObjectPool<InetSocketAddress, ConnectionSupplier> getFastDFSConnectionPool() {
		return fastDFSConnectionPool;
	}
	
	
}
 