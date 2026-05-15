package companies;

import com.nicico.committee.Application;
import com.nicico.committee.config.ApplicationException;
import com.nicico.committee.config.GlobalExceptionHandler;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.base.DescribedPredicate.alwaysTrue;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.ENUMS;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.belongToAnyOf;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * A class for testing the technical structure of the application.
 * @author Seyyed
 */
@AnalyzeClasses(packagesOf = Application.class, importOptions = DoNotIncludeTests.class)
class TechnicalStructureTest {
    /**
     * Ensures that the application's architecture follows a layered pattern, promoting separation of concerns and
     * maintainability.
     */
    @ArchTest
    static final ArchRule respectsTechnicalArchitectureLayers = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Config").definedBy("..config..")
            .layer("Controller").definedBy("..controller..")
            .optionalLayer("Service").definedBy("..service..")
            .optionalLayer("Repository").definedBy("..repository..")
            .optionalLayer("Utility").definedBy("..repository..")
            .layer("Entities").definedBy("..entities..")
            .layer("Report").definedBy("..util..")

            .whereLayer("Config").mayOnlyBeAccessedByLayers("Report","Entities","Service")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller","Report","Repository","Utility")
            .whereLayer("Report").mayOnlyBeAccessedByLayers("Service","Report","Controller","Repository")
            .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service","Report")
            .whereLayer("Entities").mayOnlyBeAccessedByLayers("Repository", "Service","Report","Controller","Config")
            .ignoreDependency(belongToAnyOf(Application.class), alwaysTrue())
            .ignoreDependency(alwaysTrue(), ENUMS)
            .ignoreDependency(alwaysTrue(), belongToAnyOf(ApplicationException.class, GlobalExceptionHandler.class))
            .because("""
                    Our architecture follows a layered pattern, promoting separation of concerns and\s
                    maintainability. Controllers handle requests, Services implement business logic, Repositories\s
                    manage data access, and Entities represent the data model. This structure enhances testability\s
                    and reduces complexity.
                   \s""");
}
