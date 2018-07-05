package com.maoding.core.component.fastdfs.conn.pool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

import com.maoding.core.component.fastdfs.ClientGlobal;
import com.maoding.core.component.fastdfs.ProtoCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 
 *   <B>说       明</B>:表示客户端与服务端的连接的抽象类。
 *
 */
public abstract class AbstractConnectionSupplier implements ConnectionSupplier {
	private static final Logger logger = LoggerFactory.getLogger(AbstractConnectionSupplier.class);
	protected static final int BUF_SIZE = 0x1000; // 4K
	protected Socket socket;
	protected final InetSocketAddress inetSockAddr;
	protected InputStream inputStream_ = null;
	protected OutputStream outputStream_ = null;
	
	public AbstractConnectionSupplier(InetSocketAddress inetSockAddr) {
		this.inetSockAddr = inetSockAddr;
		socket = new Socket();
		try {
			socket.setReuseAddress(true);
			socket.setTcpNoDelay(true);
			socket.setSoLinger(false, 0);
			socket.setSoTimeout(ClientGlobal.g_network_timeout);
		} catch (SocketException e) {
			logger.error("Could not configure socket : --> " + inetSockAddr, e);
		}
		if (!isConnected()) {
			try {
				socket.connect(inetSockAddr, ClientGlobal.g_connect_timeout);
				inputStream_ = new BufferedInputStream(socket.getInputStream(), BUF_SIZE);
				outputStream_ = new BufferedOutputStream(socket.getOutputStream(), BUF_SIZE);
			} catch (IOException iox) {
				close();
				logger.error("can't create connection to : --> "+inetSockAddr,iox);
			}
		}
	}
	@Override
	public OutputStream getOutputStream() throws IOException {
		return outputStream_;
	}
	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream_;
	}
	
	@Override
	public InetSocketAddress getInetSocketAddress() {
		return inetSockAddr;
	}

	@Override
	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed()
				&& socket.isConnected() && !socket.isInputShutdown()
				&& !socket.isOutputShutdown();
	}

	@Override
	public boolean isValid() {
		if (isConnected()) {
			try {
				return ProtoCommon.activeTest(inputStream_,outputStream_);
			} catch (IOException e) {
				logger.error("valid connection error : -->"+inetSockAddr, e);
			} finally {
				ProtoCommon.closeQuietly(socket);
			}
		}
		return false;
	}


	@Override
	public void close() {
		if (isConnected()) {
			try {
				ProtoCommon.closeSocket(inputStream_,outputStream_);
			} catch (IOException e) {
				logger.error("close connection error : -->"+inetSockAddr, e);
			} finally {
				ProtoCommon.closeQuietly(socket);
			}
		}
	}
}
 