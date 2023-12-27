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

### Stream collect to set perserving order

```java
.collect( Collectors.toCollection( LinkedHashSet::new ) );
```

from https://stackoverflow.com/questions/51944956/stream-to-linkedhashset

### Stream group by

```java
Map<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
  .collect(groupingBy(BlogPost::getType));
```

from https://www.baeldung.com/java-groupingby-collector

## GIT

### Create patchfile branch diff

```shell
git diff master Branch1 > ../patchfile
git checkout Branch2
git apply ../patchfile
```

from https://stackoverflow.com/questions/16675766/get-the-difference-between-two-branches-in-git

### Fetch and merge master without branch switch

```shell
git fetch origin master:master
git merge master
```

### Squash merge

```shell
git checkout master
git merge --squash bugfix
git commit
```

from https://stackoverflow.com/questions/5308816/how-can-i-merge-multiple-commits-onto-another-branch-as-a-single-squashed-commit

### Prevent local commits to master

```shell
touch git/hooks/pre-commit
```

```bash
#!/bin/bash
branch="$(git rev-parse --abbrev-ref HEAD)"
if [ "$branch" = "master" ]; then
  echo "You can't commit directly to master branch"
  exit 1
fi
```

```shell
chmod +x .git/hooks/pre-commit
```

from https://stackoverflow.com/questions/40462111/prevent-commits-in-master-branch
