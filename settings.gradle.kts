pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FinancialApp"
include(":app")
include(":core-ui")
include(":core-data")
include(":feature-account")
include(":feature-categories")
include(":feature-expenses")
include(":feature-incomes")
include(":feature-settings")
include(":core-network")
include(":core-domain")
