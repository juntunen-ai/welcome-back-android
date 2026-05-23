package ai.juntunen.welcomeback

/**
 * String dictionaries — a direct port of iOS `Strings.swift`.
 * Keys use dot-notation: section.subsection.item
 *
 * Add a key in BOTH dictionaries. Finnish falls back to English then to the
 * raw key if missing, so you'll see the key in the UI when something's missing.
 */
object Strings {

    // ── English ──────────────────────────────────────────────────────────────
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

        // Onboarding — Model download (stub for v0.3)
        "onboarding.model.title"           to "Download the AI",
        "onboarding.model.subtitle"        to "Welcome Back uses an on-device AI.\nYour conversations are completely private\nand work without internet.",
        "onboarding.model.card.subtitle"   to "Google on-device AI · %s · Wi-Fi recommended",
        "onboarding.model.download.button" to "Download Now · %s",
        "onboarding.model.download.later"  to "Download later",
        "onboarding.model.coming_soon"     to "On-device AI download is coming in v0.3. For now, the app works in cloud mode.",

        // Onboarding — Profile
        "onboarding.profile.title"            to "What's your name?",
        "onboarding.profile.subtitle"         to "We need your permission to use your information to support your memories. Your data always stays on your phone and is never shared anywhere else.",
        "onboarding.profile.name.label"       to "Your Name",
        "onboarding.profile.name.placeholder" to "e.g. Harri",
        "onboarding.profile.continue"         to "Continue",
        "onboarding.profile.continue.hint"    to "Enter your name first",

        // Onboarding — Photo
        "onboarding.photo.title"    to "Add Your Photo",
        "onboarding.photo.subtitle" to "A photo helps make the app feel personal and familiar.",
        "onboarding.photo.tap"      to "Tap to choose a photo",
        "onboarding.photo.continue" to "Continue",
        "onboarding.photo.skip"     to "Skip for now",

        // Onboarding — Add Family Member
        "onboarding.family.title"              to "Add a Family Member",
        "onboarding.family.subtitle"           to "Who is someone close to you? Start by adding one person — you can add more later.",
        "onboarding.family.name.label"         to "Name",
        "onboarding.family.name.placeholder"   to "e.g. Anna, Matti",
        "onboarding.family.rel.label"          to "Relationship",
        "onboarding.family.rel.placeholder"    to "e.g. Wife, Son, Daughter",
        "onboarding.family.memory.label"       to "A Memory Together",
        "onboarding.family.memory.placeholder" to "Write a few words about a special moment together, then tap Generate…",
        "onboarding.family.ai.button"          to "Generate with AI ✦",
        "onboarding.family.ai.generating"      to "Generating…",
        "onboarding.family.ai.hint"            to "Write a brief description and AI will expand it into a warm memory",
        "onboarding.family.continue"           to "Save & Continue",
        "onboarding.family.skip"               to "Add later",

        // Onboarding — Family Tip
        "onboarding.family.tip.title"    to "You Can Always Add More",
        "onboarding.family.tip.body"     to "Add more family members and memories anytime from Settings → Family Members.",
        "onboarding.family.tip.location" to "Settings → Family Members",
        "onboarding.family.tip.continue" to "Got it",

        // Onboarding — Add Place
        "onboarding.place.title"              to "Add an Important Place",
        "onboarding.place.subtitle"           to "A home, a cottage, or somewhere that holds special meaning for you.",
        "onboarding.place.name.label"         to "Place Name",
        "onboarding.place.name.placeholder"   to "e.g. Summer Cottage, Childhood Home",
        "onboarding.place.desc.label"         to "What Makes It Special",
        "onboarding.place.desc.placeholder"   to "Describe this place briefly, then tap Write with AI…",
        "onboarding.place.ai.button"          to "Write with AI ✦",
        "onboarding.place.ai.generating"      to "Writing…",
        "onboarding.place.continue"           to "Save & Continue",
        "onboarding.place.skip"               to "Skip for now",

        // Onboarding — Place Tip
        "onboarding.place.tip.title"    to "More Places Later",
        "onboarding.place.tip.body"     to "Add more important places anytime from Settings → Places.",
        "onboarding.place.tip.location" to "Settings → Places",
        "onboarding.place.tip.continue" to "Got it",

        // Onboarding — Add Story
        "onboarding.story.title"             to "Share a Memory",
        "onboarding.story.subtitle"          to "Tell the app about a moment in your life you want to hold on to.",
        "onboarding.story.title.label"       to "Title",
        "onboarding.story.title.placeholder" to "e.g. Our Wedding Day, Summer 1985",
        "onboarding.story.text.label"        to "Your Story",
        "onboarding.story.text.placeholder"  to "Write a few words or a brief description — AI can write the full story for you…",
        "onboarding.story.ai.button"         to "Let AI Write It ✦",
        "onboarding.story.ai.generating"     to "Writing your story…",
        "onboarding.story.continue"          to "Save & Continue",
        "onboarding.story.skip"              to "Skip for now",

        // Onboarding — Story Tip
        "onboarding.story.tip.title"    to "Keep Building Your Memories",
        "onboarding.story.tip.body"     to "Add more memories and stories anytime from Settings → Memories & Stories.",
        "onboarding.story.tip.location" to "Settings → Memories & Stories",
        "onboarding.story.tip.continue" to "Got it",

        // Onboarding — Music
        "onboarding.music.title"    to "Your Music Memories",
        "onboarding.music.subtitle" to "Music can unlock powerful memories. Connect Spotify to play your favourite songs from throughout your life.",
        "onboarding.music.connect"  to "Connect Spotify",
        "onboarding.music.skip"     to "Skip for now",
        "onboarding.music.note"     to "Spotify integration arrives in v0.5. For now, this step is informational.",

        // Onboarding — Complete
        "onboarding.complete.title"    to "You're all set%s!",
        "onboarding.complete.subtitle" to "Welcome Back is ready to help you\nrediscover your memories.",
        "onboarding.complete.cta"      to "Start Remembering",
        "onboarding.complete.tip1"     to "Tap the big mic button on the Home screen to start a conversation.",
        "onboarding.complete.tip2"     to "Add family members in Settings so the app can introduce them.",
        "onboarding.complete.tip3"     to "Your Memories tab shows photos and stories grouped by category.",

        // AI generation
        "onboarding.ai.error" to "Couldn't generate — try again or write it yourself.",

        // Main app placeholder (v0.4)
        "main.placeholder.title" to "Main app coming in v0.4",
        "main.placeholder.body"  to "You've completed onboarding! The home screen, memories, family, and settings are scheduled for the v0.4 release.",
        "main.placeholder.reset" to "Restart onboarding",
    )

    // ── Finnish ──────────────────────────────────────────────────────────────
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

        // Onboarding — Mallin lataus
        "onboarding.model.title"           to "Lataa tekoäly",
        "onboarding.model.subtitle"        to "Welcome Back käyttää laitteessa toimivaa tekoälyä.\nKeskustelusi ovat täysin yksityisiä\nja toimivat ilman internetyhteyttä.",
        "onboarding.model.card.subtitle"   to "Google laitteen tekoäly · %s · Wi-Fi suositellaan",
        "onboarding.model.download.button" to "Lataa nyt · %s",
        "onboarding.model.download.later"  to "Lataa myöhemmin",
        "onboarding.model.coming_soon"     to "Laitteessa toimivan tekoälyn lataus tulee versioon v0.3. Toistaiseksi sovellus toimii pilvitilassa.",

        // Onboarding — Profiili
        "onboarding.profile.title"            to "Mikä sinun nimesi on?",
        "onboarding.profile.subtitle"         to "Tarvitsemme luvan tietojesi käyttöön, jotta voimme tukea muisteluasi. Tiedot pysyvät aina puhelimellasi. Niitä ei jaeta mihinkään muualle.",
        "onboarding.profile.name.label"       to "Nimesi",
        "onboarding.profile.name.placeholder" to "esim. Harri",
        "onboarding.profile.continue"         to "Jatka",
        "onboarding.profile.continue.hint"    to "Kirjoita ensin nimesi",

        // Onboarding — Kuva
        "onboarding.photo.title"    to "Lisää oma kuvasi",
        "onboarding.photo.subtitle" to "Kuva tekee sovelluksesta henkilökohtaisemman ja tutumman.",
        "onboarding.photo.tap"      to "Napauta valitaksesi kuvan",
        "onboarding.photo.continue" to "Jatka",
        "onboarding.photo.skip"     to "Ohita toistaiseksi",

        // Onboarding — Lisää perheenjäsen
        "onboarding.family.title"              to "Lisää perheenjäsen",
        "onboarding.family.subtitle"           to "Kuka sinulle läheinen ihminen on? Lisää yksi henkilö nyt — voit lisätä lisää myöhemmin.",
        "onboarding.family.name.label"         to "Nimi",
        "onboarding.family.name.placeholder"   to "esim. Anna, Matti",
        "onboarding.family.rel.label"          to "Suhde",
        "onboarding.family.rel.placeholder"    to "esim. Vaimo, Poika, Tytär",
        "onboarding.family.memory.label"       to "Yhteinen muisto",
        "onboarding.family.memory.placeholder" to "Kirjoita muutama sana erityisestä hetkestä yhdessä, sitten napauta Generoi…",
        "onboarding.family.ai.button"          to "Generoi tekoälyllä ✦",
        "onboarding.family.ai.generating"      to "Generoidaan…",
        "onboarding.family.ai.hint"            to "Kirjoita lyhyt kuvaus ja tekoäly laajentaa sen lämpimäksi muistoksi",
        "onboarding.family.continue"           to "Tallenna ja jatka",
        "onboarding.family.skip"               to "Lisää myöhemmin",

        // Onboarding — Perheenvinkki
        "onboarding.family.tip.title"    to "Voit lisätä lisää myöhemmin",
        "onboarding.family.tip.body"     to "Lisää perheenjäseniä ja muistoja milloin tahansa kohdasta Asetukset → Perheenjäsenet.",
        "onboarding.family.tip.location" to "Asetukset → Perheenjäsenet",
        "onboarding.family.tip.continue" to "Selvä",

        // Onboarding — Lisää paikka
        "onboarding.place.title"              to "Lisää tärkeä paikka",
        "onboarding.place.subtitle"           to "Koti, mökki tai jokin paikka, jolla on erityinen merkitys sinulle.",
        "onboarding.place.name.label"         to "Paikan nimi",
        "onboarding.place.name.placeholder"   to "esim. Kesämökki, Lapsuudenkoti",
        "onboarding.place.desc.label"         to "Miksi se on erityinen",
        "onboarding.place.desc.placeholder"   to "Kuvaile paikkaa lyhyesti, sitten napauta Kirjoita tekoälyllä…",
        "onboarding.place.ai.button"          to "Kirjoita tekoälyllä ✦",
        "onboarding.place.ai.generating"      to "Kirjoitetaan…",
        "onboarding.place.continue"           to "Tallenna ja jatka",
        "onboarding.place.skip"               to "Ohita toistaiseksi",

        // Onboarding — Paikkavinkki
        "onboarding.place.tip.title"    to "Lisää paikkoja myöhemmin",
        "onboarding.place.tip.body"     to "Lisää tärkeitä paikkoja milloin tahansa kohdasta Asetukset → Paikat.",
        "onboarding.place.tip.location" to "Asetukset → Paikat",
        "onboarding.place.tip.continue" to "Selvä",

        // Onboarding — Lisää tarina
        "onboarding.story.title"             to "Jaa muisto",
        "onboarding.story.subtitle"          to "Kerro sovellukselle hetkestä elämässäsi, jonka haluat muistaa.",
        "onboarding.story.title.label"       to "Otsikko",
        "onboarding.story.title.placeholder" to "esim. Häämme, Kesä 1985",
        "onboarding.story.text.label"        to "Muistosi",
        "onboarding.story.text.placeholder"  to "Kirjoita muutama sana tai lyhyt kuvaus — tekoäly voi kirjoittaa täyden tarinan puolestasi…",
        "onboarding.story.ai.button"         to "Anna tekoälyn kirjoittaa ✦",
        "onboarding.story.ai.generating"     to "Kirjoitetaan tarinaasi…",
        "onboarding.story.continue"          to "Tallenna ja jatka",
        "onboarding.story.skip"              to "Ohita toistaiseksi",

        // Onboarding — Tarinavinkki
        "onboarding.story.tip.title"    to "Jatka muistojen rakentamista",
        "onboarding.story.tip.body"     to "Lisää muistoja ja tarinoita milloin tahansa kohdasta Asetukset → Muistot ja tarinat.",
        "onboarding.story.tip.location" to "Asetukset → Muistot ja tarinat",
        "onboarding.story.tip.continue" to "Selvä",

        // Onboarding — Musiikki
        "onboarding.music.title"    to "Musiikkimuistosi",
        "onboarding.music.subtitle" to "Musiikki voi avata voimakkaita muistoja. Yhdistä Spotify soittaaksesi suosikkikappaleitasi elämäsi varrelta.",
        "onboarding.music.connect"  to "Yhdistä Spotify",
        "onboarding.music.skip"     to "Ohita toistaiseksi",
        "onboarding.music.note"     to "Spotify-integraatio tulee versioon v0.5. Toistaiseksi tämä vaihe on informatiivinen.",

        // Onboarding — Valmis
        "onboarding.complete.title"    to "Kaikki on valmiina%s!",
        "onboarding.complete.subtitle" to "Welcome Back on valmis auttamaan sinua\nmuistamaan muistosi.",
        "onboarding.complete.cta"      to "Aloita muistelu",
        "onboarding.complete.tip1"     to "Napauta isoa mikrofoninappia kotinäytöllä aloittaaksesi keskustelun.",
        "onboarding.complete.tip2"     to "Lisää perheenjäseniä asetuksissa, jotta sovellus voi esitellä heidät.",
        "onboarding.complete.tip3"     to "Muistot-välilehti näyttää kuvat ja tarinat kategorioittain.",

        // Tekoälyn generointi
        "onboarding.ai.error" to "Generointi epäonnistui — yritä uudelleen tai kirjoita itse.",

        // Pääsovelluksen placeholder (v0.4)
        "main.placeholder.title" to "Pääsovellus tulee versioon v0.4",
        "main.placeholder.body"  to "Olet suorittanut onboardingin! Kotinäyttö, muistot, perhe ja asetukset ovat aikataulutettu v0.4-julkaisuun.",
        "main.placeholder.reset" to "Aloita onboarding uudelleen",
    )
}
