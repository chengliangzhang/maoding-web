package com.maoding.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * Created by Idccapp22 on 2016/8/22.
 */
public abstract class SerializeTranscoder {

    protected final Logger log= LoggerFactory.getLogger(this.getClass());

    public abstract byte[] serialize(Object value);

    public abstract Object deserialize(byte[] in);

    public void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                log.info("Unable to close " + closeable, e);
            }
        }
    }
}
