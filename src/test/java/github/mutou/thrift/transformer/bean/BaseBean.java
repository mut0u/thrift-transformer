package github.mutou.thrift.transformer.bean;

import lombok.ToString;

@ToString
public class BaseBean {
    public boolean bool1;
    public byte byte1;
    public int i161;
    public int i321;
    public long i641;
    public double d1;
    public String s1;

    public BaseBean set(boolean bool1) {
        this.bool1 = bool1;
        return this;
    }

    public BaseBean set(byte byte1) {
        this.byte1 = byte1;
        return this;
    }

    public BaseBean set(String s1) {
        this.s1 = s1;
        return this;
    }
}
