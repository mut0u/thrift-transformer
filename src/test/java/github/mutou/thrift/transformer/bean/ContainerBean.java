package github.mutou.thrift.transformer.bean;

import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
public class ContainerBean {
    public List<String> sList;
    public List<BaseBean> baseList;
    public Map<String, String> m;
}
