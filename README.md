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

### Database link

https://docs.oracle.com/cd/B19306_01/server.102/b14200/statements_5005.htm

## Hibernate

SQL logging

https://stackoverflow.com/questions/1710476/how-to-print-a-query-string-with-parameter-values-when-using-hibernate

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

### Stream toMap

```java
books.stream().collect(Collectors.toMap(Book::getIsbn, Book::getName));
```

from https://www.baeldung.com/java-collectors-tomap

### Stream shared predicate state

```java
public IntPredicate sharedState() {
    final List<Integer> l = new ArrayList<>();
    return curr -> {
        if (l.size() >= 3) {
            return true;
        }
        l.add(curr);
        return false;
    };
}

@Test
void streamWithSharedPredicate() {
    var shared = sharedState();
    var x = IntStream.range(0, 20)
            .filter(shared)
            .toArray();
    System.out.println(x.length); // 17
    x = IntStream.range(0, 20)
            .filter(shared)
            .toArray();
    System.out.println(x.length); // 20
}
```

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

### Revert multiple commits

```shell
$ git revert --no-commit D
$ git revert --no-commit C
$ git revert --no-commit B
$ git commit -m "the commit message for all of them"
```

from https://stackoverflow.com/questions/1463340/how-can-i-revert-multiple-git-commits

### Reset to remote branch

To save uncommited changes: git stash

```shell
git fetch --all
git branch backup-master
git reset --hard origin/master
```

from https://stackoverflow.com/questions/1125968/how-do-i-force-git-pull-to-overwrite-local-files

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

## Linux

### Free up space in boot

```shell
sudo apt-get autoremove
```

from https://askubuntu.com/questions/89710/how-do-i-free-up-more-space-in-boot
