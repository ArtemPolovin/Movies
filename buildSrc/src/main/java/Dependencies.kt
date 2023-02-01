object Versions {
    const val kotlin = "1.6.0"
    const val coreKtx = "1.3.2"
    const val appcompat = "1.2.0"
    const val material = "1.4.0"
    const val constraintlayout = "2.0.4"
    const val junit = "4.12"
    const val junitTest = "1.1.2"
    const val espressoCore = "3.3.0"
    const val navFragment = "2.5.2"
    const val recyclerView = "1.2.1"
    const val retrofit = "2.9.0"
    const val daggerHilt = "2.38.1"
    const val glide = "4.12.0"
    const val serializationJson = "1.2.1"
    const val coroutines = "1.5.2"
    const val room = "2.4.3"
    const val gson = "2.8.6"
    const val legacy = "1.0.0"
    const val pref_ktx = "1.2.0"
    const val paging = "3.1.1"
    const val youtubePlayer = "10.0.5"
    const val anim = "2.4@aar"
    const val gif = "4.1.0"
    const val coordinatorLayout = "1.2.0"
    const val retrofitKtx = "0.9.2"
    const val workMangerKtx = "2.7.1"
    const val workManagerHilt = "1.0.0"
    const val firebase_analytics = "firebase-analytics-ktx"
    const val  firebase_crashlytics = "firebase-crashlytics-ktx"
}

object Deps {
    const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val googleMaterial = "com.google.android.material:material:${Versions.material}"
    const val constraintlayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintlayout}"
    const val junit = "junit:junit:${Versions.junit}"
    const val junitTest = "androidx.test.ext:junit:${Versions.junitTest}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val serialization =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.serializationJson}"
    const val gson= "com.google.code.gson:gson:${Versions.gson}"
    const val legacy = "androidx.legacy:legacy-support-v4:${Versions.legacy}"
    const val pref_ktx = "androidx.preference:preference-ktx:${Versions.pref_ktx}"
    const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
    const val youtubePlayer = "com.pierfrancescosoffritti.androidyoutubeplayer:core:${Versions.youtubePlayer}"
    const val animation = "com.daimajia.androidanimations:library:${Versions.anim}"
    const val gif = "com.airbnb.android:lottie:${Versions.gif}"
    const val coordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinatorLayout}"
    const val retrofit_kotlin_coroutines_adapter = "com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:${Versions.retrofitKtx}"
    const val work_manager_ktx = "androidx.work:work-runtime-ktx:${Versions.workMangerKtx}"
    const val work_manager_hilt = "androidx.hilt:hilt-work:${Versions.workManagerHilt}"
    const val work_manager_hilt_compiler = "androidx.hilt:hilt-compiler:${Versions.workManagerHilt}"
    const val firebase_analytics_ktx =  "com.google.firebase:${Versions.firebase_analytics}"
    const val firebase_crashlytics_ktx =  "com.google.firebase:${Versions.firebase_crashlytics}"

    //Coroutines
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
    const val coroutinesAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

    //Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val  roomKtx=       "androidx.room:room-ktx:${Versions.room}"

    //Retrofit 2
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val converterGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"

    //Dagger Hilt
    const val daggerHilt = "com.google.dagger:hilt-android:${Versions.daggerHilt}"
    const val daggerHiltCompiler = "com.google.dagger:hilt-compiler:${Versions.daggerHilt}"

    //Navigation Fragment
    const val navFragment = "androidx.navigation:navigation-fragment-ktx:${Versions.navFragment}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Versions.navFragment}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navFragment}"


}
