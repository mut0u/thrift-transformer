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
        System.out.println(r);
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
        System.out.println(u);
    }


}
