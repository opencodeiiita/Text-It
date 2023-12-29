buildscript {
    val agp_version by extra("8.1.3")
    dependencies {
            classpath("com.google.gms:google-services:4.4.0")
            classpath ("com.android.tools.build:gradle:$agp_version")// use the latest version available
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
}
