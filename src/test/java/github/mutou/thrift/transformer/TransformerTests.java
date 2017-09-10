package github.mutou.thrift.transformer;

import github.mutou.thrift.demo.BaseStruct;
import github.mutou.thrift.demo.ContainerStruct;
import github.mutou.thrift.demo.Request;
import github.mutou.thrift.transformer.bean.BaseBean;
import github.mutou.thrift.transformer.bean.UserTestBean;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static github.mutou.thrift.transformer.Transformer.fromJava;
import static github.mutou.thrift.transformer.Transformer.toJava;

public class TransformerTests {


    @Test
    public void fromJavaTest() throws TransformException {

        Request r = fromJava(new Object() {
            public String name = "michael";
            public Object o = new Object() {
                public String name = "savior";
                public int t = 10;
            };
        }, Request.class);
        assert r != null;
        assert r.name == "michael";
        assert r.o.name == "savior";
        assert r.o.t == 10;
    }

    @Test
    public void toJavaTest() throws TransformException {

        Request r = fromJava(new Object() {
            public String name = "michael";
            public Object o = new Object() {
                public String name = "savior";
                public int t = 10;
            };
        }, Request.class);

        UserTestBean u = toJava(r, UserTestBean.class);
        assert u.name == "michael";
        assert u.o.name == "savior";
        assert u.o.t == 10;
    }


    @Test
    public void transferBaseTypeTest() throws TransformException {

        BaseStruct r = fromJava(new Object() {
            public boolean bool1 = true;
            public byte byte1 = 10;
            public int i161 = 11;
            public int i321 = -40;
            public long i641 = 100;
            public double d1 = 5.6;
            public String s1 = "hello";
        }, BaseStruct.class);
        assert r != null && r.bool1 && r.byte1 == 10 && r.i161 == 11 && r.i321 == -40 && r.i641 == 100 && r.d1 == 5.6 && r.s1 == "hello";
    }

    @Test
    public void transferContainerTypeTest() throws TransformException {
        Map<String, String> mm = new HashMap<String, String>();
        mm.put("k1", "v1");
        mm.put("k2", "v2");
        ContainerStruct r = fromJava(new Object() {
            public List<String> sList = Arrays.asList("aa", "bb");
            public List<BaseBean> baseList = Arrays.asList(
                    new BaseBean().set("b1"),
                    new BaseBean().set("b2"));
            public Map<String, String> m = mm;

        }, ContainerStruct.class);
        System.out.println(r);
        assert r.sList != null && !r.sList.isEmpty() && r.sList.get(0) == "aa";
        assert r != null && r.m != null && !r.m.isEmpty() && r.m.get("k2") == "v2";
    }

}
