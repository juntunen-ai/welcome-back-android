package ai.juntunen.welcomeback

/**
 * String dictionaries — a direct port of iOS `Strings.swift`.
 * Keys use dot-notation: section.subsection.item
 *
 * Add a key in BOTH dictionaries. Finnish falls back to English then to the
 * raw key if missing, so you'll see the key in the UI when something's missing.
 *
 * For v0.1 this is a starter subset covering the welcome / language screens.
 * The full port will mirror Strings.swift line-by-line over the coming days.
 */
object Strings {

    val en: Map<String, String> = mapOf(
        // Common actions
        "common.cancel"   to "Cancel",
        "common.save"     to "Save",
        "common.continue" to "Continue",
        "common.skip"     to "Skip",
        "common.ok"       to "OK",
        "common.add"      to "Add",
        "common.done"     to "Done",
        "common.delete"   to "Delete",

        // Tabs
        "tab.home"     to "Home",
        "tab.memories" to "Memories",
        "tab.family"   to "Family",
        "tab.music"    to "Music",
        "tab.settings" to "Settings",

        // Onboarding — Welcome
        "onboarding.welcome.title"    to "Welcome Back",
        "onboarding.welcome.subtitle" to "A compassionate companion\nfor your most precious memories.",
        "onboarding.welcome.cta"      to "Get Started",
        "onboarding.welcome.time"     to "Set up takes about 1 minute",

        // Onboarding — Language picker
        "onboarding.language.title"    to "Choose your language",
        "onboarding.language.subtitle" to "You can change this later in Settings.",
        "onboarding.language.continue" to "Continue",

        // Onboarding — Profile
        "onboarding.profile.title"            to "What's your name?",
        "onboarding.profile.subtitle"         to "We need your permission to use your information to support your memories. Your data always stays on your phone and is never shared anywhere else.",
        "onboarding.profile.name.label"       to "Your Name",
        "onboarding.profile.name.placeholder" to "e.g. Harri",
        "onboarding.profile.continue"         to "Continue",
    )

    val fi: Map<String, String> = mapOf(
        // Yleiset toiminnot
        "common.cancel"   to "Peruuta",
        "common.save"     to "Tallenna",
        "common.continue" to "Jatka",
        "common.skip"     to "Ohita",
        "common.ok"       to "OK",
        "common.add"      to "Lisää",
        "common.done"     to "Valmis",
        "common.delete"   to "Poista",

        // Välilehdet
        "tab.home"     to "Koti",
        "tab.memories" to "Muistot",
        "tab.family"   to "Perhe",
        "tab.music"    to "Musiikki",
        "tab.settings" to "Asetukset",

        // Onboarding — Tervetuloa
        "onboarding.welcome.title"    to "Welcome Back",
        "onboarding.welcome.subtitle" to "Välittävä kumppani\nkalleimmille muistoillesi.",
        "onboarding.welcome.cta"      to "Aloita",
        "onboarding.welcome.time"     to "Käyttöönotto kestää noin minuutin",

        // Onboarding — Kielivalinta
        "onboarding.language.title"    to "Valitse kieli",
        "onboarding.language.subtitle" to "Voit muuttaa tätä myöhemmin asetuksissa.",
        "onboarding.language.continue" to "Jatka",

        // Onboarding — Profiili
        "onboarding.profile.title"            to "Mikä sinun nimesi on?",
        "onboarding.profile.subtitle"         to "Tarvitsemme luvan tietojesi käyttöön, jotta voimme tukea muisteluasi. Tiedot pysyvät aina puhelimellasi. Niitä ei jaeta mihinkään muualle.",
        "onboarding.profile.name.label"       to "Nimesi",
        "onboarding.profile.name.placeholder" to "esim. Harri",
        "onboarding.profile.continue"         to "Jatka",
    )
}
