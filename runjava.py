from jnius import autoclass
from java import MetaJavaClass, JavaClass, JavaMethod, JavaStaticMethod

Stack = autoclass('java.util.Stack')
stack = Stack()
stack.push('hello')
stack.push('world')
print stack.pop()
# 'world'
print stack.pop()
# 'hello'

class Hardware(JavaClass):
    __metaclass__ = MetaJavaClass
    __javaclass__ = 'group39'
    java_distance = JavaMethod('()V')


print "test", Hardware.java_distance()