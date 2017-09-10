package github.mutou.thrift.transformer;


import org.apache.thrift.TBase;
import org.apache.thrift.TFieldIdEnum;
import org.apache.thrift.meta_data.FieldMetaData;
import org.apache.thrift.meta_data.StructMetaData;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class Transformer {


    private static TFieldIdEnum getFieldByName(Class fieldsClass, String name) throws TransformException {
        Class[] cArg = new Class[1];
        cArg[0] = String.class;
        Method method = null;
        try {
            method = fieldsClass.getDeclaredMethod("findByName", cArg);
        } catch (NoSuchMethodException e) {
            throw new TransformException("can not find the method named : findByName", e);
        }
        try {
            return (TFieldIdEnum) method.invoke(fieldsClass, name);
        } catch (IllegalAccessException e) {
            throw new TransformException("can not call the method named : findByName", e);
        } catch (InvocationTargetException e) {
            throw new TransformException("can not call the method named : findByName", e);
        }
    }


    public static <T extends TBase> T fromJava(Object o, Class<T> clz) throws TransformException {

        T message = null;
        try {
            message = clz.newInstance();
            Map metaDataMap = (Map) clz.getDeclaredField("metaDataMap").get(message);
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().contains("$")) {
                    continue;
                }
                Field f = message.getClass().getDeclaredField(field.getName());
                TFieldIdEnum fieldEntity = getFieldByName(clz.getClasses()[0], field.getName());
                FieldMetaData fieldMeta = (FieldMetaData) metaDataMap.get(fieldEntity);
                if (fieldMeta == null) {
                    continue;
                }
                Object innerObject = field.get(o);
                if (fieldMeta.valueMetaData.isStruct()) {
                    TBase messageInner = fromJava(innerObject, ((StructMetaData) fieldMeta.valueMetaData).structClass);
                    f.set(message, messageInner);
                } else {
                    switch (fieldMeta.valueMetaData.type) {
                        case 6:
                            Object so = field.get(o);
                            short v = 0;
                            if (so instanceof Number) {
                                v = ((Integer) so).shortValue();
                            }
                            f.set(message, v);
                            break;
                            /*
                            case 13:
                                System.out.println("aaa");
                                break;
                            */
                        default:
                            f.set(message, field.get(o));
                            break;
                    }
                }
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        }
        return message;
    }

    public static <T, F extends TBase> T toJava(F message, Class<T> clz) throws TransformException {
        T o = null;
        try {
            o = clz.newInstance();
            Map metaDataMap = null;
            metaDataMap = (Map) message.getClass().getDeclaredField("metaDataMap").get(message);

            for (Field field : message.getClass().getFields()) {
                if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                Field oField = clz.getDeclaredField(field.getName());
                if (oField == null) {
                    continue;
                }

                TFieldIdEnum fieldEntity = getFieldByName(message.getClass().getClasses()[0], field.getName());
                FieldMetaData fieldMeta = (FieldMetaData) metaDataMap.get(fieldEntity);
                if (fieldMeta == null) {
                    continue;
                }
                if (fieldMeta.valueMetaData.isStruct()) {
                    Object objectInner = toJava((F) field.get(message), oField.getType());
                    oField.set(o, objectInner);
                } else {
                    oField.set(o, field.get(message));
                }
            }


        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        }
        return o;
    }
}
