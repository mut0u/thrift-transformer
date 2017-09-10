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