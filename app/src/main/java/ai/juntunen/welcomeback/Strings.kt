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

        // Common
        "back"   to "Back",
        "cancel" to "Cancel",
        "save"   to "Save",
        "delete" to "Delete",

        // Home screen
        "home.greeting"          to "Good day",
        "home.intro.subtitle"    to "What would you like to remember today?",
        "home.mic.label"         to "Tap to talk",
        "home.mic.accessibility" to "Start voice conversation",
        "home.location.finding"  to "Finding your location…",
        "home.location.unavailable" to "Location unavailable",
        "home.family.header"     to "YOUR FAMILY",
        "home.about.title"       to "About Welcome Back",
        "home.about.body"        to "Welcome Back is a compassionate AI companion that helps you recall memories, recognize family and friends, and feel more confident throughout your day.",

        // Memories screen
        "memories.title"          to "Memories",
        "memories.family.header"  to "FAMILY",
        "memories.places.header"  to "IMPORTANT PLACES",
        "memories.stories.header" to "STORIES & MEMORIES",
        "memories.empty"          to "No memories yet.\nAdd family members, places, and stories in Settings.",
        "memories.story.hear"     to "Hear This Story",

        // Memory categories
        "memory.category.family" to "Family",
        "memory.category.events" to "Events",
        "memory.category.places" to "Places",
        "memory.category.other"  to "Other",

        // Place detail
        "place.open.maps" to "Open in Maps",

        // Family screens
        "family.title"      to "Family",
        "family.empty"      to "No family members yet.\nTap + in Settings to add someone.",
        "family.add.title"  to "Add Family Member",
        "family.edit.title" to "Edit Family Member",
        "family.add.name"   to "Name",
        "family.add.name.placeholder"         to "e.g. Anna",
        "family.add.relationship"             to "Relationship",
        "family.add.relationship.placeholder" to "e.g. Wife, Son, Daughter",
        "family.add.phone"                    to "Phone",
        "family.add.phone.placeholder"        to "e.g. +358 40 123 4567",
        "family.add.biography"                to "About Them",
        "family.add.biography.placeholder"    to "A few words about this person…",
        "family.add.memory1"                  to "Memory 1",
        "family.add.memory1.placeholder"      to "A special shared memory…",
        "family.add.memory2"                  to "Memory 2",
        "family.add.memory2.placeholder"      to "Another cherished memory…",
        "family.edit.delete"                  to "Remove Family Member",
        "family.edit.delete.confirm.title"    to "Remove Family Member",
        "family.edit.delete.confirm.body"     to "This will permanently remove this person and their memories.",

        // Family profile
        "family.profile.biography" to "ABOUT",
        "family.profile.memory1"   to "MEMORY",
        "family.profile.memory2"   to "ANOTHER MEMORY",
        "family.profile.hear"      to "Hear %s's Story",
        "family.profile.stop"      to "Stop",

        // Music screen
        "music.title"           to "Music",
        "music.subtitle"        to "Music & Memories",
        "music.body"            to "Music has a powerful ability to unlock memories from the past. Connect Spotify to play songs that have meant the most to you throughout your life.",
        "music.connect.spotify" to "Connect Spotify",
        "music.coming.soon"     to "Spotify integration is coming in v0.5",

        // Listening / voice session
        "listening.title"       to "I'm Listening",
        "listening.hint"        to "Tap the button below to start talking",
        "listening.listening"   to "Listening…",
        "listening.thinking"    to "Thinking…",
        "listening.speaking"    to "Speaking…",
        "listening.error"       to "Something went wrong",
        "listening.stop"        to "Stop",
        "listening.start"       to "Start Talking",
        "listening.end"         to "End",
        "listening.connecting"  to "Connecting…",
        "listening.ready"       to "I'm listening",
        "listening.go_on"       to "Go on…",
        "listening.moment"      to "One moment…",
        "listening.response"    to "Speaking…",
        "listening.ended"       to "Session ended",

        // Settings
        "settings.title"               to "Settings",
        "settings.profile.unnamed"     to "Your Name",
        "settings.profile.edit"        to "Edit personal information",
        "settings.section.content"     to "Content",
        "settings.section.preferences" to "Preferences",
        "settings.section.legal"       to "Legal",
        "settings.family"              to "Family Members",
        "settings.family.count"        to "%d members",
        "settings.memories"            to "Memories & Stories",
        "settings.memories.count"      to "%d memories",
        "settings.places"              to "Important Places",
        "settings.places.count"        to "%d places",
        "settings.notifications"       to "Notifications",
        "settings.language"            to "Language",
        "settings.voice.mode"          to "Voice Mode",
        "settings.model"               to "AI Model",
        "settings.privacy"             to "Privacy Policy",
        "settings.terms"               to "Terms of Use",
        "settings.reset"               to "Reset & Start Over",
        "settings.reset.confirm.title" to "Reset Everything?",
        "settings.reset.confirm.body"  to "All your data — family, memories, places, and photos — will be deleted and you'll return to the welcome screen. This cannot be undone.",
        "settings.version"             to "Welcome Back v%s",

        // Personal info
        "settings.personal.title"               to "Personal Info",
        "settings.personal.name"                to "Your Name",
        "settings.personal.name.placeholder"    to "e.g. Harri",
        "settings.personal.address"             to "Home Address",
        "settings.personal.address.placeholder" to "e.g. Mannerheimintie 1, Helsinki",
        "settings.personal.biography"           to "About You",
        "settings.personal.biography.placeholder" to "A few sentences about yourself…",

        // Family management
        "settings.family.title" to "Family Members",
        "settings.family.empty" to "No family members yet.\nTap + to add someone close to you.",
        "settings.family.add"   to "Add Family Member",

        // Memories management
        "settings.memories.title" to "Memories & Stories",
        "settings.memories.empty" to "No memories yet.\nTap + to add a story or memory.",

        // Memory edit
        "settings.memory.add.title"            to "Add Memory",
        "settings.memory.edit.title"           to "Edit Memory",
        "settings.memory.photo.hint"           to "Tap to add a photo",
        "settings.memory.title.label"          to "Title",
        "settings.memory.title.placeholder"    to "e.g. Our Wedding Day",
        "settings.memory.date.label"           to "Date",
        "settings.memory.date.placeholder"     to "e.g. June 1985",
        "settings.memory.category.label"       to "Category",
        "settings.memory.description.label"    to "Story",
        "settings.memory.description.placeholder" to "Write the memory here…",
        "settings.memory.delete"               to "Delete Memory",
        "settings.memory.delete.confirm.title" to "Delete Memory?",
        "settings.memory.delete.confirm.body"  to "This memory will be permanently deleted.",

        // Places management
        "settings.places.title" to "Important Places",
        "settings.places.empty" to "No places yet.\nTap + to add a place that means something to you.",

        // Place edit
        "settings.place.add.title"              to "Add Place",
        "settings.place.edit.title"             to "Edit Place",
        "settings.place.photo.hint"             to "Tap to add a photo",
        "settings.place.name.label"             to "Place Name",
        "settings.place.name.placeholder"       to "e.g. Summer Cottage",
        "settings.place.description.label"      to "What Makes It Special",
        "settings.place.description.placeholder" to "Describe this place…",
        "settings.place.delete"                 to "Delete Place",
        "settings.place.delete.confirm.title"   to "Delete Place?",
        "settings.place.delete.confirm.body"    to "This place will be permanently deleted.",

        // Notifications
        "settings.notifications.title"                to "Notifications",
        "settings.notifications.enable"               to "Daily Reminders",
        "settings.notifications.enable.subtitle"      to "Gentle check-ins throughout your day",
        "settings.notifications.times"                to "REMINDER TIMES",
        "settings.notifications.morning"              to "Morning",
        "settings.notifications.noon"                 to "Noon",
        "settings.notifications.afternoon"            to "Afternoon",
        "settings.notifications.evening"              to "Evening",
        "settings.notifications.topics.header"        to "WHAT TO REMIND YOU ABOUT",
        "settings.notifications.topics.placeholder"   to "e.g. family names, important dates, home address…",

        // Language
        "settings.language.title" to "Language",
        "settings.language.note"  to "Changing the language affects all text in the app.",

        // Voice mode
        "settings.voice.title"           to "Voice Mode",
        "settings.voice.cloud"           to "Cloud (Gemini)",
        "settings.voice.cloud.subtitle"  to "Requires internet. Best accuracy.",
        "settings.voice.local"           to "On-Device",
        "settings.voice.local.subtitle"  to "Works offline. Coming in v0.3.",
        "settings.voice.note"            to "On-device voice processing arrives in v0.3.",

        // Model settings
        "settings.model.title"        to "AI Model",
        "settings.model.current"      to "Currently using",
        "settings.model.active"       to "Active Model",
        "settings.model.cloud.title"  to "Gemini 2.0 Flash (Cloud)",
        "settings.model.cloud.body"   to "A fast, conversational AI model from Google. Requires an internet connection. Your messages are processed on Google's servers.",
        "settings.model.local.title"  to "On-Device Model",
        "settings.model.local.body"   to "A private AI that runs entirely on your phone — no internet required. Download coming in v0.3.",
        "settings.model.coming.soon"  to "v0.3",

        // Licenses
        "settings.licenses" to "Open-Source Licenses",

        // Onboarding — Permissions
        "onboarding.permissions.title"        to "App Permissions",
        "onboarding.permissions.subtitle"     to "Welcome Back works best with these permissions. They are optional but recommended.",
        "onboarding.permissions.mic.title"    to "Microphone",
        "onboarding.permissions.mic.subtitle" to "Required for voice conversations with the AI companion.",
        "onboarding.permissions.music.title"  to "Music Library",
        "onboarding.permissions.music.subtitle" to "Used to play your personal music collection in the Music tab.",
        "onboarding.permissions.granted"      to "Granted",
        "onboarding.permissions.tap_to_grant" to "Tap to grant",

        // Music screen
        "music.memorylane.subtitle" to "these are your favorite songs",
        "music.library.header"      to "YOUR LIBRARY",
        "music.mixes.header"        to "MEMORY MIXES",
        "music.library.empty"       to "No songs found on your device.",
        "music.connect.title"       to "Connect Your Music",
        "music.connect.subtitle"    to "Allow access to play songs from your device library.",
        "music.connect.button"      to "Allow Access",
        "music.denied.title"        to "Music Access Denied",
        "music.denied.body"         to "Go to System Settings → Apps → Welcome Back → Permissions to allow music access.",
        "music.mix.1"               to "Morning Calm",
        "music.mix.2"               to "Classic Favourites",
        "music.mix.3"               to "Family Moments",
        "music.mix.4"               to "Relaxation",
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

        // Yleiset
        "back"   to "Takaisin",
        "cancel" to "Peruuta",
        "save"   to "Tallenna",
        "delete" to "Poista",

        // Kotinäyttö
        "home.greeting"          to "Hyvää päivää",
        "home.intro.subtitle"    to "Mitä haluaisit muistella tänään?",
        "home.mic.label"         to "Napauta puhuaksesi",
        "home.mic.accessibility" to "Aloita äänipuhelu",
        "home.location.finding"  to "Etsitään sijaintiasi…",
        "home.location.unavailable" to "Sijainti ei saatavilla",
        "home.family.header"     to "PERHEESI",
        "home.about.title"       to "Tietoa Welcome Backista",
        "home.about.body"        to "Welcome Back on välittävä tekoälykumppani, joka auttaa sinua muistelemaan, tunnistamaan perhettäsi ja ystäviäsi ja tuntemaan olosi varmemmaksi päivän aikana.",

        // Muistot-näyttö
        "memories.title"          to "Muistot",
        "memories.family.header"  to "PERHE",
        "memories.places.header"  to "TÄRKEÄT PAIKAT",
        "memories.stories.header" to "TARINAT JA MUISTOT",
        "memories.empty"          to "Ei muistoja vielä.\nLisää perheenjäseniä, paikkoja ja tarinoita asetuksissa.",
        "memories.story.hear"     to "Kuuntele tämä tarina",

        // Muistokategoriat
        "memory.category.family" to "Perhe",
        "memory.category.events" to "Tapahtumat",
        "memory.category.places" to "Paikat",
        "memory.category.other"  to "Muut",

        // Paikkadetaljit
        "place.open.maps" to "Avaa Kartassa",

        // Perhenäytöt
        "family.title"      to "Perhe",
        "family.empty"      to "Ei perheenjäseniä vielä.\nNapauta + asetuksissa lisätäksesi jonkun.",
        "family.add.title"  to "Lisää perheenjäsen",
        "family.edit.title" to "Muokkaa perheenjäsentä",
        "family.add.name"   to "Nimi",
        "family.add.name.placeholder"         to "esim. Anna",
        "family.add.relationship"             to "Suhde",
        "family.add.relationship.placeholder" to "esim. Vaimo, Poika, Tytär",
        "family.add.phone"                    to "Puhelin",
        "family.add.phone.placeholder"        to "esim. +358 40 123 4567",
        "family.add.biography"                to "Hänestä",
        "family.add.biography.placeholder"    to "Muutama sana tästä henkilöstä…",
        "family.add.memory1"                  to "Muisto 1",
        "family.add.memory1.placeholder"      to "Erityinen yhteinen muisto…",
        "family.add.memory2"                  to "Muisto 2",
        "family.add.memory2.placeholder"      to "Toinen rakkaus muisto…",
        "family.edit.delete"                  to "Poista perheenjäsen",
        "family.edit.delete.confirm.title"    to "Poista perheenjäsen?",
        "family.edit.delete.confirm.body"     to "Tämä poistaa pysyvästi tämän henkilön ja heidän muistonsa.",

        // Perheprofiili
        "family.profile.biography" to "HÄNESTÄ",
        "family.profile.memory1"   to "MUISTO",
        "family.profile.memory2"   to "TOINEN MUISTO",
        "family.profile.hear"      to "Kuuntele %s:n tarina",
        "family.profile.stop"      to "Pysäytä",

        // Musiikkinäyttö
        "music.title"           to "Musiikki",
        "music.subtitle"        to "Musiikki ja muistot",
        "music.body"            to "Musiikilla on voimakas kyky avata muistoja menneisyydestä. Yhdistä Spotify soittaaksesi kappaleita, jotka ovat merkinneet sinulle eniten elämäsi varrella.",
        "music.connect.spotify" to "Yhdistä Spotify",
        "music.coming.soon"     to "Spotify-integraatio tulee versioon v0.5",

        // Kuuntelu / äänipuhelu
        "listening.title"       to "Kuuntelen",
        "listening.hint"        to "Napauta nappia aloittaaksesi puhumisen",
        "listening.listening"   to "Kuunnellaan…",
        "listening.thinking"    to "Mietitään…",
        "listening.speaking"    to "Puhutaan…",
        "listening.error"       to "Jokin meni pieleen",
        "listening.stop"        to "Pysäytä",
        "listening.start"       to "Aloita puhuminen",
        "listening.end"         to "Lopeta",
        "listening.connecting"  to "Yhdistetään…",
        "listening.ready"       to "Kuuntelen",
        "listening.go_on"       to "Jatka…",
        "listening.moment"      to "Hetki…",
        "listening.response"    to "Puhutaan…",
        "listening.ended"       to "Istunto päättyi",

        // Asetukset
        "settings.title"               to "Asetukset",
        "settings.profile.unnamed"     to "Nimesi",
        "settings.profile.edit"        to "Muokkaa henkilötietoja",
        "settings.section.content"     to "Sisältö",
        "settings.section.preferences" to "Asetukset",
        "settings.section.legal"       to "Oikeudellinen",
        "settings.family"              to "Perheenjäsenet",
        "settings.family.count"        to "%d jäsentä",
        "settings.memories"            to "Muistot ja tarinat",
        "settings.memories.count"      to "%d muistoa",
        "settings.places"              to "Tärkeät paikat",
        "settings.places.count"        to "%d paikkaa",
        "settings.notifications"       to "Ilmoitukset",
        "settings.language"            to "Kieli",
        "settings.voice.mode"          to "Äänentunnistus",
        "settings.model"               to "Tekoälymalli",
        "settings.privacy"             to "Tietosuojaseloste",
        "settings.terms"               to "Käyttöehdot",
        "settings.reset"               to "Nollaa ja aloita alusta",
        "settings.reset.confirm.title" to "Nollataan kaikki?",
        "settings.reset.confirm.body"  to "Kaikki tietosi — perhe, muistot, paikat ja kuvat — poistetaan pysyvästi ja palataan tervetulonäyttöön. Tätä ei voi peruuttaa.",
        "settings.version"             to "Welcome Back v%s",

        // Henkilötiedot
        "settings.personal.title"               to "Henkilötiedot",
        "settings.personal.name"                to "Nimesi",
        "settings.personal.name.placeholder"    to "esim. Harri",
        "settings.personal.address"             to "Kotiosoite",
        "settings.personal.address.placeholder" to "esim. Mannerheimintie 1, Helsinki",
        "settings.personal.biography"           to "Sinusta",
        "settings.personal.biography.placeholder" to "Muutama lause itsestäsi…",

        // Perheenhallinta
        "settings.family.title" to "Perheenjäsenet",
        "settings.family.empty" to "Ei perheenjäseniä vielä.\nNapauta + lisätäksesi sinulle läheisen henkilön.",
        "settings.family.add"   to "Lisää perheenjäsen",

        // Muistojenhallinta
        "settings.memories.title" to "Muistot ja tarinat",
        "settings.memories.empty" to "Ei muistoja vielä.\nNapauta + lisätäksesi tarinan tai muiston.",

        // Muiston muokkaus
        "settings.memory.add.title"            to "Lisää muisto",
        "settings.memory.edit.title"           to "Muokkaa muistoa",
        "settings.memory.photo.hint"           to "Napauta lisätäksesi kuvan",
        "settings.memory.title.label"          to "Otsikko",
        "settings.memory.title.placeholder"    to "esim. Häämme",
        "settings.memory.date.label"           to "Päivämäärä",
        "settings.memory.date.placeholder"     to "esim. Kesäkuu 1985",
        "settings.memory.category.label"       to "Kategoria",
        "settings.memory.description.label"    to "Tarina",
        "settings.memory.description.placeholder" to "Kirjoita muisto tähän…",
        "settings.memory.delete"               to "Poista muisto",
        "settings.memory.delete.confirm.title" to "Poistetaanko muisto?",
        "settings.memory.delete.confirm.body"  to "Tämä muisto poistetaan pysyvästi.",

        // Paikkojenhallinta
        "settings.places.title" to "Tärkeät paikat",
        "settings.places.empty" to "Ei paikkoja vielä.\nNapauta + lisätäksesi sinulle merkityksellisen paikan.",

        // Paikan muokkaus
        "settings.place.add.title"              to "Lisää paikka",
        "settings.place.edit.title"             to "Muokkaa paikkaa",
        "settings.place.photo.hint"             to "Napauta lisätäksesi kuvan",
        "settings.place.name.label"             to "Paikan nimi",
        "settings.place.name.placeholder"       to "esim. Kesämökki",
        "settings.place.description.label"      to "Miksi se on erityinen",
        "settings.place.description.placeholder" to "Kuvaile tätä paikkaa…",
        "settings.place.delete"                 to "Poista paikka",
        "settings.place.delete.confirm.title"   to "Poistetaanko paikka?",
        "settings.place.delete.confirm.body"    to "Tämä paikka poistetaan pysyvästi.",

        // Ilmoitukset
        "settings.notifications.title"                to "Ilmoitukset",
        "settings.notifications.enable"               to "Päivittäiset muistutukset",
        "settings.notifications.enable.subtitle"      to "Lempeät muistelutukset päivän aikana",
        "settings.notifications.times"                to "MUISTUTUSAJAT",
        "settings.notifications.morning"              to "Aamu",
        "settings.notifications.noon"                 to "Keskipäivä",
        "settings.notifications.afternoon"            to "Iltapäivä",
        "settings.notifications.evening"              to "Ilta",
        "settings.notifications.topics.header"        to "MISTÄ MUISTUTTAA",
        "settings.notifications.topics.placeholder"   to "esim. perheen nimet, tärkeät päivät, kotiosoite…",

        // Kieli
        "settings.language.title" to "Kieli",
        "settings.language.note"  to "Kielen vaihto vaikuttaa kaikkeen tekstiin sovelluksessa.",

        // Äänentunnistus
        "settings.voice.title"           to "Äänentunnistus",
        "settings.voice.cloud"           to "Pilvi (Gemini)",
        "settings.voice.cloud.subtitle"  to "Vaatii internetin. Paras tarkkuus.",
        "settings.voice.local"           to "Laitteessa",
        "settings.voice.local.subtitle"  to "Toimii ilman nettiä. Tulossa v0.3:ssa.",
        "settings.voice.note"            to "Laitetason äänenkäsittely tulee versioon v0.3.",

        // Mallin asetukset
        "settings.model.title"        to "Tekoälymalli",
        "settings.model.current"      to "Käytössä tällä hetkellä",
        "settings.model.active"       to "Aktiivinen malli",
        "settings.model.cloud.title"  to "Gemini 2.0 Flash (Pilvi)",
        "settings.model.cloud.body"   to "Nopea keskusteleva tekoälymalli Googlelta. Vaatii internetyhteyden. Viestisi käsitellään Googlen palvelimilla.",
        "settings.model.local.title"  to "Laitemalli",
        "settings.model.local.body"   to "Yksityinen tekoäly, joka toimii kokonaan puhelimellasi — ei internet-yhteyttä tarvita. Lataus tulee v0.3:ssa.",
        "settings.model.coming.soon"  to "v0.3",

        // Lisenssit
        "settings.licenses" to "Avoimen lähdekoodin lisenssit",

        // Onboarding — Käyttöoikeudet
        "onboarding.permissions.title"          to "Sovelluksen käyttöoikeudet",
        "onboarding.permissions.subtitle"       to "Welcome Back toimii parhaiten näiden käyttöoikeuksien kanssa. Ne ovat valinnaisia mutta suositeltavia.",
        "onboarding.permissions.mic.title"      to "Mikrofoni",
        "onboarding.permissions.mic.subtitle"   to "Tarvitaan äänipuheluihin tekoälykumppanin kanssa.",
        "onboarding.permissions.music.title"    to "Musiikkikirjasto",
        "onboarding.permissions.music.subtitle" to "Käytetään henkilökohtaisen musiikkikirjastosi toistamiseen Musiikki-välilehdellä.",
        "onboarding.permissions.granted"        to "Myönnetty",
        "onboarding.permissions.tap_to_grant"   to "Napauta myöntääksesi",

        // Musiikkinäyttö
        "music.memorylane.subtitle" to "nämä ovat suosikkikappaleesi",
        "music.library.header"      to "KIRJASTOSI",
        "music.mixes.header"        to "MUISTOMIKSIT",
        "music.library.empty"       to "Laitteeltasi ei löytynyt kappaleita.",
        "music.connect.title"       to "Yhdistä musiikkisi",
        "music.connect.subtitle"    to "Anna lupa toistaa kappaleita laitekirjastostasi.",
        "music.connect.button"      to "Anna lupa",
        "music.denied.title"        to "Musiikkipääsy estetty",
        "music.denied.body"         to "Siirry Järjestelmäasetukset → Sovellukset → Welcome Back → Käyttöoikeudet salliaksesi musiikkipääsyn.",
        "music.mix.1"               to "Aamuhetki",
        "music.mix.2"               to "Klassiset suosikit",
        "music.mix.3"               to "Perhehetket",
        "music.mix.4"               to "Rentoutuminen",
    )
}
