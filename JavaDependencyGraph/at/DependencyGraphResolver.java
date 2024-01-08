package at;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;

import java.util.*;
import java.util.stream.Collectors;

import static at.KlassType.IFACE;
import static at.KlassType.KLASS;

public class DependencyGraphResolver {

    //<archunit.version>1.0.1</archunit.version>
    //<dependency>
    //     <groupId>com.tngtech.archunit</groupId>
    //     <artifactId>archunit-junit5</artifactId>
    //     <version>${archunit.version}</version>
    //     <scope>test</scope>
    // </dependency>

    public static void visual(String includePackageName, String startFrom) {
        List<String> excludePackageEquals = List.of();
        List<String> excludePackageEndsWith = List.of(
                ".entity",
                ".domain",
                ".enums",
                ".converter",
                ".mapper",
                ".common",
                ".dto",
                ".modules",
                ".value"
        );
        List<String> excludePackageContains = List.of(".db.entity", ".test", "seed", "sync");
        List<String> excludeClassNameContains = List.of("Configuration", "java", "springframework");
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(includePackageName)
                .that(new DescribedPredicate<>("Remove classes in test package") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        String packageName = javaClass.getPackageName();
                        if (javaClass.isEnum()) {
                            return false;
                        }
                        if (javaClass.isNestedClass()) {
                            return false;
                        }
                        if (javaClass.isInterface()) {
                            return false;
                        }
                        if (excludePackageEquals.stream().anyMatch(a -> Objects.equals(a, packageName))) {
                            return false;
                        }
                        if (excludePackageEndsWith.stream().anyMatch(packageName::endsWith)) {
                            return false;
                        }
                        if (excludePackageContains.stream().anyMatch(packageName::contains)) {
                            return false;
                        }
                        return true;
                    }
                });

        JavaClasses interfaces = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages(includePackageName)
                .that(new DescribedPredicate<>("Remove classes in test package") {
                    @Override
                    public boolean test(JavaClass javaClass) {
                        String packageName = javaClass.getPackageName();
                        if (javaClass.isEnum()) {
                            return false;
                        }
                        if (javaClass.isNestedClass()) {
                            return false;
                        }
                        if (excludePackageContains.stream().anyMatch(packageName::contains)) {
                            return false;
                        }
                        return true;
                    }
                });

        var klassMap = new HashMap<String, Klass>();

        interfaces.stream()
                .filter(JavaClass::isInterface)
                .filter(a -> filterStuff(a, excludeClassNameContains))
                .forEach(a -> klassMap.put(a.getName(), Klass.of(a.getName(), IFACE)));
        classes.stream()
                .filter(a -> filterStuff(a, excludeClassNameContains))
                .forEach(a -> klassMap.put(a.getName(), Klass.of(a.getName(), KLASS)));
        classes.forEach(a -> addKlass(a, klassMap));
        classes.forEach(a -> addIFace(a, klassMap));
        klassMap.values().stream().filter(a -> !a.impls().isEmpty()).forEach(DependencyGraphResolver::addIFaceReverse);
        klassMap.values().stream().filter(a -> !a.children().isEmpty()).forEach(DependencyGraphResolver::addKlassReverse);
        Set<Klass> toRemove = klassMap.values().stream().filter(DependencyGraphResolver::allEmpty).collect(Collectors.toSet());
        toRemove.forEach(a -> klassMap.remove(a.name()));

        List<Klass> list = klassMap.values().stream().filter(a -> a.implementedBy().size() == 1).toList();

        list.forEach(DependencyGraphResolver::cleanUpIFace);
        list.forEach(a -> klassMap.remove(a.name()));

        var rootElement = Klass.of("root", KLASS);
        klassMap.values().stream()
                .filter(a -> a.impls().isEmpty())
                .filter(a -> a.implementedBy().isEmpty())
                .filter(a -> a.parents().isEmpty())
                .forEach(a -> rootElement.children().add(a));
        klassMap.values().stream()
                .filter(a -> a.impls().isEmpty())
                .filter(a -> a.implementedBy().isEmpty())
                .filter(a -> a.parents().isEmpty())
                .forEach(a -> a.parents().add(rootElement));

        Klass klass = klassMap.values().stream().filter(a -> a.name().contains(startFrom)).findAny().orElse(rootElement);
        var result = recursivePrinting(klass, rootElement == klass, new LinkedList<>());
        result.forEach(System.out::print);
        System.out.println(result.size());
    }

    private static List<String> recursivePrinting(Klass klass, boolean isRoot, List<String> list) {
        if (!isRoot) {
            klass.children().forEach(a -> list.add(klass.shortName() + "," + a.shortName() + ",depends\n"));
            klass.implementedBy().forEach(a -> list.add(klass.shortName() + "," + a.shortName() + ",implements\n"));
        }
        klass.children().forEach(a -> recursivePrinting(a, false, list));
        return list;
    }

    private static void cleanUpIFace(Klass iFace) {
        var klass = iFace.implementedBy().stream().findAny().orElseThrow();
        iFace.parents().forEach(a -> a.children().remove(iFace));
        iFace.parents().stream().filter(a -> a != klass).forEach(a -> a.children().add(klass));
        klass.parents().addAll(iFace.parents());
        iFace.implementedBy().forEach(a -> a.impls().clear());
        klass.impls().clear();
    }

    private static boolean allEmpty(Klass a) {
        return a.implementedBy().isEmpty()
                && a.impls().isEmpty()
                && a.parents().isEmpty()
                && a.children().isEmpty();
    }

    private static boolean filterStuff(JavaClass javaClass, List<String> list) {
        var name = javaClass.getName();
        return list.stream().noneMatch(name::contains);
    }

    private static void addIFace(JavaClass aClass, HashMap<String, Klass> klassSet) {
        Klass e = klassSet.get(aClass.getName());
        for (JavaClass allRawInterface : aClass.getAllRawInterfaces()) {
            var javaClasses = klassSet.get(allRawInterface.getFullName());
            if (javaClasses != null && e != javaClasses) {
                e.impls().add(javaClasses);
            }
        }
    }

    private static void addIFaceReverse(Klass aClass) {
        aClass.impls().forEach(a -> a.implementedBy().add(aClass));
    }

    private static void addKlassReverse(Klass aClass) {
        aClass.children().forEach(a -> a.parents().add(aClass));
    }

    private static void addKlass(JavaClass aClass, HashMap<String, Klass> klassSet) {
        Klass e = klassSet.get(aClass.getName());
        for (JavaField field : aClass.getFields()) {
            if (!field.getModifiers().contains(JavaModifier.STATIC)) {
                var x = klassSet.get(aClass.getFullName());
                if (x != null) {
                    Klass e1 = klassSet.get(field.getRawType().getName());
                    if (e1 != null && e1 != e) {
                        e.children().add(e1);
                    }
                }
            }
        }
    }
}
