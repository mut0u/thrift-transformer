namespace java github.mutou.thrift.demo

typedef i32 int


service UserService {
  User find(1:Request r);
}


struct User{
  1:string name;
  2:int age;
}
struct O{
  1:string name;
  2:int t;
}

struct Request{
  1:string name;
  2:O o;
}

struct SetStruct{
  1:i16 i1;
  2:i32 i2;
  3:i64 i3;
  4:double i4;
  5:string i5;
  6:byte i6;
}



struct BaseStruct{
  1:bool bool1;
  2:byte byte1;
  3:i16 i161;
  4:i32 i321;
  5:i64 i641;
  7:double d1;
  8:string s1;
}
struct ContainerStruct{
  1:list<string> sList;
  2:list<BaseStruct> baseList;
  3:map<string,string> m;
}