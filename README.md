# notes23

### Unit tests

return argument as result:

```java
when(myMock.myFunction(anyString())).thenAnswer(i -> i.getArguments()[0]);
```

from https://stackoverflow.com/questions/2684630/making-a-mocked-method-return-an-argument-that-was-passed-to-it
