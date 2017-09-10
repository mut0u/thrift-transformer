package github.mutou.thrift.transformer.bean;

import github.mutou.thrift.transformer.bean.OTestBean;
import lombok.Data;

@Data
public class UserTestBean {
    public String name;
    public OTestBean o;
}
