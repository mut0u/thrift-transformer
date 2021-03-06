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
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new TransformException("can not call the method named : findByName", e);
        }
    }

    private static String setMethodName(String fileName){
        if(fileName ==null){
            return null;
        } else if (fileName.length() ==1){
            return "set" + Character.toUpperCase(fileName.charAt(0));
         } else {
            return "set" + Character.toUpperCase(fileName.charAt(0)) + fileName.substring(1);
        }

    }

    public static <T extends TBase> T fromJava(Object o, Class<T> clz) throws TransformException {

        T message = null;
        try {
            message = clz.newInstance();
            Map metaDataMap;
            try {
                metaDataMap = (Map) clz.getDeclaredField("metaDataMap").get(message);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
                throw new TransformException("transfer exception", e);
            }
            for (Field field : o.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().contains("$")) {
                    continue;
                }
                Field f;
                try {
                    f = message.getClass().getDeclaredField(field.getName());
                } catch (NoSuchFieldException e) {
                    System.out.println("can not find the field");
                    continue;
                }
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
                            // todo case 8 , 3, 4
                        case 10:
                            try {
                                Method setMethod = message.getClass().getDeclaredMethod(setMethodName(fieldMeta.fieldName), long.class);
                              if (setMethod != null) {
                                setMethod.invoke(message, field.get(o));
                              }
                            } catch (NoSuchMethodException |InvocationTargetException e) {
                                e.printStackTrace();
                            }
                            break;
                            case 16:
                                try {
                                  Method tmpMethod = f.getType().getDeclaredMethod("findByValue", int.class);
                                    f.set(message , tmpMethod.invoke(f.getType(), field.get(o)));
                                } catch (NoSuchMethodException | InvocationTargetException e) {
                                    e.printStackTrace();
                                }

                                break;

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
                } else {
                    oField.setAccessible(true);
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

                     if(field.get(message) instanceof org.apache.thrift.TEnum) {
                         Integer tempObject =  ((org.apache.thrift.TEnum) field.get(message)).getValue();
                         if(oField.getType() == int.class){
                             oField.set(o, tempObject);
                         } else if (oField.getType() == String.class) {
                             oField.set(o, tempObject.toString());
                         }

                     } else {
                         oField.set(o, field.get(message));
                     }
                }
            }


        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
            throw new TransformException("transfer exception", e);
        }
      return o;
    }
}
