# notes23

## Unit tests

### return argument as result:

```java
when(myMock.myFunction(anyString())).thenAnswer(i -> i.getArguments()[0]);
```

from https://stackoverflow.com/questions/2684630/making-a-mocked-method-return-an-argument-that-was-passed-to-it

## Oracle SQL

### limit results

```SQL
SELECT val
FROM   rownum_order_test
ORDER BY val DESC
FETCH FIRST 5 ROWS ONLY;
```

from https://stackoverflow.com/questions/470542/how-do-i-limit-the-number-of-rows-returned-by-an-oracle-query-after-ordering

## Java

### Sort based on subproperty

```java
entities.sort(Comparator.comparing(entity -> entity.getSubEntity().getAmount()));
```

from https://stackoverflow.com/questions/48095224/java-8-sort-on-class-members-property