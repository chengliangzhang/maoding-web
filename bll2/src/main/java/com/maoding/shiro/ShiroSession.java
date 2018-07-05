package com.maoding.shiro;

/**
 * Created by Wuwq on 2016/11/14.
 */
import org.apache.shiro.session.mgt.SimpleSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;


/**
 * 由于SimpleSession lastAccessTime更改后也会调用SessionDao update方法，
 * 增加标识位，如果只是更新lastAccessTime SessionDao update方法直接返回
 */
public class ShiroSession extends SimpleSession implements Serializable {
    private static final long serialVersionUID = -7136803329764600351L;
    private Logger logger = LoggerFactory.getLogger(ShiroSession.class);

    private boolean needUpdate;
	
	/*Add a logic. When current date is over the timeout, update the redis side and notified
	all tomcat server to update session*/

    public ShiroSession() {
        super();
        logger.debug("ShiroSession is created");
        this.needUpdate = true;
    }

    @Override
    public void setId(Serializable id) {
        super.setId(id);
        logger.debug("Session id is set");
        this.needUpdate = true;
    }

    @Override
    public void setLastAccessTime(Date lastAccessTime) {
        logger.debug("Session lastAccessTime is set");
        super.setLastAccessTime(lastAccessTime);
        this.needUpdate = false;
    }

    @Override
    public void setStartTimestamp(Date startTimestamp) {
        super.setStartTimestamp(startTimestamp);
        logger.debug("Session startTimestamp is set");
        this.needUpdate = true;
    }

    @Override
    public void setStopTimestamp(Date stopTimestamp) {
        super.setStopTimestamp(stopTimestamp);
        logger.debug("Session stopTimestamp is set");
        this.needUpdate = true;
    }

    @Override
    public void setExpired(boolean expired) {
        super.setExpired(expired);
        logger.debug("Session expired is set");
        this.needUpdate = true;
    }

    @Override
    public boolean isExpired() {
        return super.isExpired();
    }

    @Override
    protected boolean isStopped() {
        return super.isStopped();
    }

    @Override
    public void setTimeout(long timeout) {
        super.setTimeout(timeout);
        logger.debug("Session timeout is set");
        this.needUpdate = true;
    }

    @Override
    public void setHost(String host) {
        super.setHost(host);
        logger.debug("Session host is set");
        this.needUpdate = true;
    }

    @Override
    public void setAttributes(Map<Object, Object> attributes) {
        super.setAttributes(attributes);
        logger.debug("Session attributes is set");
        this.needUpdate = true;
    }

    @Override
    public void touch() {
        super.touch();
        /*logger.debug("Session is touched");*/
        this.needUpdate = false;
    }

    @Override
    public void stop() {
        super.stop();
        logger.debug("Session stop is set");
        //Listener will do this part
        //this.needUpdate = true;
        //this.needNotified = false;
    }

    @Override
    protected void expire() {
        super.expire();
        logger.debug("Session expire is set");
        //Listener will do this part
        //this.needUpdate = true;
        //this.needNotified = false;
    }

    @Override
    public void setAttribute(Object key, Object value) {
        super.setAttribute(key, value);
        logger.debug("Session attribute is set");
        this.needUpdate = true;
    }

    @Override
    public Object removeAttribute(Object key) {
        this.needUpdate = true;
        logger.debug("Session attribute is removed");
        return super.removeAttribute(key);
    }

    @Override
    protected boolean onEquals(SimpleSession ss) {
        return super.onEquals(ss);
    }

    public boolean isNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(boolean needUpdate) {
        this.needUpdate = needUpdate;
    }


}
