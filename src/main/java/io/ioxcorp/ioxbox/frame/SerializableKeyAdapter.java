package io.ioxcorp.ioxbox.frame;

import java.awt.event.KeyAdapter;
import java.io.Serializable;

/**
 * why sonarlint
 * @author ioxom
 */
public class SerializableKeyAdapter extends KeyAdapter implements Serializable {
    private static final long serialVersionUID = -7065349752989953979L;

    public SerializableKeyAdapter() {
        super();
    }
}
