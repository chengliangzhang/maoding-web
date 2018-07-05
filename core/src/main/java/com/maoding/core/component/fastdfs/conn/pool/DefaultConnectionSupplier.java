package com.maoding.core.component.fastdfs.conn.pool;

import java.net.InetSocketAddress;
import java.net.SocketException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <B>说 明</B>:默认客户端与服务端的连接。
 *
 */
public class DefaultConnectionSupplier extends AbstractConnectionSupplier {

	private static final Logger logger = LoggerFactory.getLogger(DefaultConnectionSupplier.class);
	private volatile boolean isReturn;
	public DefaultConnectionSupplier(InetSocketAddress inetSockAddr) {
		super(inetSockAddr);
		try {
			socket.setKeepAlive(true);
		} catch (SocketException sx) {
			logger.error("Could not configure socket.", sx);
		}
	}

	@Override
	public boolean isReturnConn() {
		return isReturn;
	}

	@Override
	public void setReturnConn(boolean isReturnconn) {
		isReturn = isReturnconn;
	}
}
