package com.maoding.core.component.fastdfs.conn.pool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/** 
 *   <B>说       明</B>:表示一个客户端与服务端的连接供应商。
 *
 */
public interface ConnectionSupplier {
	void close();
	boolean isConnected();
	boolean isValid();
	InetSocketAddress getInetSocketAddress();
	OutputStream getOutputStream() throws IOException;
	InputStream getInputStream() throws IOException; 
	boolean isReturnConn();
	void setReturnConn(boolean isReturnconn);
}
 