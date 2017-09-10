package github.mutou.thrift.transformer;

import github.mutou.thrift.demo.Request;
import org.junit.Test;

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


}
