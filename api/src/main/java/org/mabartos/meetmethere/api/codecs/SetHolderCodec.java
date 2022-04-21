package org.mabartos.meetmethere.api.codecs;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class SetHolderCodec<T> implements MessageCodec<SetHolder<T>, SetHolder<T>> {
    @Override
    public void encodeToWire(Buffer buffer, SetHolder<T> holder) {

    }

    @Override
    public SetHolder<T> decodeFromWire(int pos, Buffer buffer) {
        return null;
    }

    @Override
    public SetHolder<T> transform(SetHolder<T> holder) {
        return holder;
    }

    @Override
    public String name() {
        return "SetHolderCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
