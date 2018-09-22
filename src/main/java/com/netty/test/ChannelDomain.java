package com.netty.test;

import io.netty.channel.Channel;

public class ChannelDomain {
    private Channel local;
    private String  localStr;
    private String  remoteStr;
    private Channel remote;
    private int index;

    public Channel getLocal() {
        return local;
    }

    public void setLocal(Channel local) {
        this.local = local;
    }

    public String getLocalStr() {
        return localStr;
    }

    public void setLocalStr(String localStr) {
        this.localStr = localStr;
    }

    public String getRemoteStr() {
        return remoteStr;
    }

    public void setRemoteStr(String remoteStr) {
        this.remoteStr = remoteStr;
    }

    public Channel getRemote() {
        return remote;
    }

    public void setRemote(Channel remote) {
        this.remote = remote;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        return "ChannelDomain{" +
                "local=" + local +
                ", localStr='" + localStr + '\'' +
                ", remoteStr='" + remoteStr + '\'' +
                ", remote=" + remote +
                ", index=" + index +
                '}';
    }
}
