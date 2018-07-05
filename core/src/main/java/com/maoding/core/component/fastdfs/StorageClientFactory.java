package com.maoding.core.component.fastdfs;

import java.io.IOException;
import java.util.Map;

import com.maoding.core.common.factory.BaseFactory;
import com.maoding.core.component.fastdfs.common.FastDFSException;
import com.maoding.core.component.fastdfs.conn.pool.ConnectionSupplier;


/**深圳市设计同道技术有限公司
 * 类    名：StorageClientFactory
 * 类描述：
 * 作    者：Chenxj
 * 日    期：2016年3月29日-上午10:31:08
 */
public class StorageClientFactory extends BaseFactory{
	private static StorageClientFactory storageClientFactory;
	private StorageClientFactory(){};
	public static synchronized StorageClientFactory getInstance(){
		if(storageClientFactory==null){
			storageClientFactory=new StorageClientFactory();
		}
		return storageClientFactory;
	}
	@Override
	public void setInitProperties(Map<String, String> initProperties) {
		super.setInitProperties(initProperties);
		try {
			ClientGlobal.init(initProperties);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FastDFSException e) {
			e.printStackTrace();
		}
	}
	public StorageClient createClient() throws Exception{
		TrackerClient tracker=new TrackerClient();
		ConnectionSupplier connection = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient client=new StorageClient(connection,storageServer);
		return client;
	}
	public StorageClient1 createClient1() throws Exception {
		TrackerClient tracker=new TrackerClient();
		ConnectionSupplier connection = tracker.getConnection();
		StorageServer storageServer = null;
		StorageClient1 client=new StorageClient1(connection,storageServer);
		return client;
	}
}
